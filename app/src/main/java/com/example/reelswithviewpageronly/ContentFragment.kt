package com.example.reelswithviewpageronly

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import fi.finwe.orion360.sdk.pro.OrionContext
import fi.finwe.orion360.sdk.pro.OrionScene
import fi.finwe.orion360.sdk.pro.item.OrionCamera
import fi.finwe.orion360.sdk.pro.item.OrionPanorama
import fi.finwe.orion360.sdk.pro.licensing.LicenseManager
import fi.finwe.orion360.sdk.pro.licensing.LicenseStatus
import fi.finwe.orion360.sdk.pro.texture.OrionTexture
import fi.finwe.orion360.sdk.pro.view.OrionView
import fi.finwe.orion360.sdk.pro.view.OrionViewContainer
import fi.finwe.orion360.sdk.pro.viewport.OrionDisplayViewport

private const val TAG = "ContentFragment"

private const val ARG_VIDEO = "ARG_VIDEO"
private const val ARG_TITLE = "ARG_TITLE"

class ContentFragment : Fragment() {
    private var video: String? = null
    private var title: String? = null

    /** Context.  */
    protected var mContext: Context? = null

    /** Root view.  */
    protected var mRootView: View? = null

    /**
     * OrionContext used to be a static class, but starting from Orion360 3.1.x it must
     * be instantiated as a member.
     */
    protected var mOrionContext: OrionContext? = null

    /**
     * The Android view (SurfaceView), where our 3D scene (OrionView) will be added to.
     */
    protected var mViewContainer: OrionViewContainer? = null

    /**
     * OrionView is an internal Orion360 class that it uses for presenting rendered content
     * on the device's display.
     *
     * Further, OrionViewport defines a part of an OrionView that is used for rendering an
     * OrionScene using an OrionCamera, on that particular area of the OrionView.
     *
     * In most cases, the OrionView contains just a single OrionViewport that comprises the
     * full OrionView, or two OrionViewports side by side to be used for rendering a separate
     * image for both eyes when using a VR headset. Yet, other configurations are possible.
     */
    protected var mView: OrionView? = null

    /**
     * OrionScene is a collection of content that is present in a rendered OrionView. The content
     * consists of a set of visible artefacts (OrionSceneItem) and programs that manipulate them
     * (OrionController).
     */
    protected var mScene: OrionScene? = null

    /**
     * OrionPanorama is an OrionSceneItem that specializes in rendering equirectangular textures.
     *
     * In most cases, the texture is wrapped around the inner surface of a polygon (sphere,
     * diamond etc.) and the camera placed at the center of the polygon, which presents a
     * beautiful panoramic view of the given texture.
     *
     * In other cases, planar surfaces are used to render the texture in original (source)
     * format or with a special effect such as the little planet projection.
     *
     * OrionPanorama is also responsible for handling the cases where different parts of the
     * source texture needs to be mapped into different parts of the polygon, and cases where
     * the source texture contains less than full 360x180 degree panorama image (e.g. doughnut).
     */
    protected var mPanorama: OrionPanorama? = null

    /**
     * OrionTexture represents a graphical image such as a still image (OrionImageTexture),
     * video (OrionVideoTexture) or camera preview image (OrionCameraTexture).
     *
     * In order to render the texture into the scene, the OrionTexture needs to be bound to an
     * OrionSceneItem that defines the object's shape, location, size and orientation in the 3D
     * rendering world. This OrionSceneItem may be an OrionSprite (for planar shapes) or
     * OrionPanorama (for spherical shapes).
     */
    protected var mPanoramaTexture: OrionTexture? = null

    /**
     * OrionCamera is an OrionSceneItem that defines the viewing parameters in the 3D rendering
     * world, such as the location, orientation and the field of view (FOV) of the viewer.
     *
     * The OrionCamera can be bound to an OrionViewport or an OrionView. When bound to an
     * OrionView, the camera is used as a default for each OrionViewport that does not have
     * another explicitly bound camera.
     */
    protected var mCamera: OrionCamera? = null

    /**
     * Convenience class for configuring typical touch control logic, where dragging gestures
     * are mapped to panning the view, and pinch gestures to zooming/rotating the view.
     *
     * In addition, a special mathematical algorithm Auto Horizon Aligner (AHL), here used via
     * its component RotationAligner, keeps the horizon straight when user is not looking towards
     * nadir (down) or zenith (up). This allows free navigation within the sphere, without any
     * artificial limits - a feature that is unique to Orion360.
     */
    protected var mTouchController: TouchControllerWidget? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            video = it.getString(ARG_VIDEO)
            title = it.getString(ARG_TITLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Propagate fragment lifecycle callbacks to the OrionContext object (singleton).
        mOrionContext = OrionContext()
        mOrionContext!!.onCreate(mContext as Activity?)

        // Perform Orion360 license check. A valid license file should be put to /assets folder!
        verifyOrionLicense()

        // Use a layout that has OrionVideoView.
        mRootView = inflater.inflate(
            R.layout.fragment_content,
            container, false
        )

        // Configure Orion360 for playing full spherical monoscopic videos/images.
        configureOrionView()

        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: video : $video")
        Log.d(TAG, "onViewCreated: title : $title")

        val titleTextView : TextView = view.findViewById(R.id.title)
        titleTextView.text = title
    }

    override fun onStart() {
        super.onStart()
        // Propagate fragment lifecycle callbacks to the OrionContext object (singleton).
        mOrionContext!!.onStart()
    }

    override fun onResume() {
        super.onResume()
        // Propagate fragment lifecycle callbacks to the OrionContext object (singleton).
        mOrionContext!!.onResume()
    }

    override fun onPause() {
        // Propagate fragment lifecycle callbacks to the OrionContext object (singleton).
        mOrionContext!!.onPause()
        super.onPause()
    }

    override fun onStop() {
        // Propagate fragment lifecycle callbacks to the OrionContext object (singleton).
        mOrionContext!!.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        // Propagate fragment lifecycle callbacks to the OrionContext object (singleton).
        mOrionContext!!.onDestroy()
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
    }

    /**
     * Verify Orion360 license. This is the first thing to do after creating OrionContext.
     */
    protected fun verifyOrionLicense() {
        val licenseManager = mOrionContext!!.licenseManager
        val licenses = LicenseManager.findLicensesFromAssets(mContext)
        for (license in licenses) {
            val verifier = licenseManager.verifyLicense(mContext, license)
            Log.i(
                TAG,
                "Orion360 license " + verifier.licenseSource.uri + " verified: "
                        + verifier.licenseStatus
            )
            if (verifier.licenseStatus == LicenseStatus.OK) break
        }
    }

    /**
     * Configure Orion360 for playing full spherical monoscopic videos/images.
     */
    private fun configureOrionView() {

        // For compatibility with Google Cardboard 1.0 with magnetic switch, disable
        // magnetometer from sensor fusion. Also recommended for devices with poorly
        // calibrated magnetometer.
        mOrionContext!!.sensorFusion.setMagnetometerEnabled(false)

        // Create a new scene. This represents a 3D world where various objects can be placed.
        mScene = OrionScene(mOrionContext)

        // Bind sensor fusion as a controller. This will make it available for scene objects.
        mScene!!.bindRoutine(mOrionContext!!.sensorFusion)

        // Create a new panorama. This is a 3D object that will represent a spherical
        // video/image.
        mPanorama = OrionPanorama(mOrionContext)

        // Create a new video (or image) texture from a video (or image) source URI.
        mPanoramaTexture = OrionTexture.createTextureFromURI(
            mOrionContext, mContext,
            video
        )

        // Bind the panorama texture to the panorama object. Here we assume full spherical
        // equirectangular monoscopic source, and wrap the complete texture around the sphere.
        // If you have stereoscopic content or doughnut shape video, use other method variants.
        mPanorama!!.bindTextureFull(0, mPanoramaTexture)

        // Bind the panorama to the scene. This will make it part of our 3D world.
        mScene!!.bindSceneItem(mPanorama)

        // Create a new camera. This will become the end-user's eyes into the 3D world.
        mCamera = OrionCamera(mOrionContext)

        // Define maximum limit for zooming. As an example, at value 1.0 (100%) zooming in is
        // disabled. At 3.0 (300%) camera will never reduce the FOV below 1/3 of the base FOV.
        mCamera!!.zoomMax = 3.0f

        // Set yaw angle to 0. Now the camera will always point to the same yaw angle
        // (to the horizontal center point of the equirectangular video/image source)
        // when starting the app, regardless of the orientation of the device.
        mCamera!!.setDefaultRotationYaw(0f)

        // Bind camera as a controllable to sensor fusion. This will let sensors rotate
        // the camera.
        mOrionContext!!.sensorFusion.bindControllable(mCamera)

        // Create a new touch controller widget (convenience class), and let it control
        // our camera.
        mTouchController = TouchControllerWidget(mOrionContext, mCamera)

        // Bind the touch controller widget to the scene. This will make it functional
        // in the scene.
        mScene!!.bindWidget(mTouchController)

        // Find Orion360 view container from the XML layout. This is an Android view for content.
        mViewContainer =
            mRootView!!.findViewById<View>(R.id.orion_view_container) as OrionViewContainer

        // Create a new OrionView and bind it into the container.
        mView = OrionView(mOrionContext)
        mViewContainer!!.bindView(mView)

        // Bind the scene to the view. This is the 3D world that we will be rendering
        // to this view.
        mView!!.bindDefaultScene(mScene)

        // Bind the camera to the view. We will look into the 3D world through this camera.
        mView!!.bindDefaultCamera(mCamera)

        // The view can be divided into one or more viewports. For example, in VR mode we have one
        // viewport per eye. Here we fill the complete view with one (landscape) viewport.
        mView!!.bindViewports(
            OrionDisplayViewport.VIEWPORT_CONFIG_FULL,
            OrionDisplayViewport.CoordinateType.FIXED_PORTRAIT
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(video: String="", title: String="") =
            ContentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_VIDEO, video)
                    putString(ARG_TITLE, title)
                }
            }
    }
}