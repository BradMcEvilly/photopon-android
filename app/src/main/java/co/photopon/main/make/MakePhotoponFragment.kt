package co.photopon.main.make

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.*
import co.photopon.R
import co.photopon.extensions.mainApplication
import co.photopon.image.CompareSizesByArea
import co.photopon.image.ImageSaver
import co.photopon.main.MainViewPagerAdapter.Companion.ARGUMENT_PRESELECTED_COUPON
import co.photopon.main.gifts.GiftViewModel
import co.photopon.views.AutoFitTextureView
import kotlinx.android.synthetic.main.fragment_make_photopon.*
import kotlinx.android.synthetic.main.fragment_make_photopon.view.*
import java.io.File
import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MakePhotoponFragment : Fragment(), MakePhotoponView {

  private var selectedPage = 0
  private var viewModels: ArrayList<GiftViewModel>? = null

  interface MakePhotoponListener {
    fun onCameraError()

    fun onStorageError()

    fun onPictureTaken(file: File, coupon: GiftViewModel?)
  }

  @Inject
  lateinit var presenter: MakePhotoponPresenter

  private var callback: MakePhotoponListener? = null

  override fun onAttach(context: Context?) {

    Log.i(TAG, "---------====---------")
    Log.i(TAG, "onAttach")
    Log.i(TAG, "---------====---------")

    super.onAttach(context)

    callback = context as? MakePhotoponListener
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {

    Log.i(TAG, "---------====---------")
    Log.i(TAG, "onCreateView")
    Log.i(TAG, "---------====---------")

    mainApplication.appComponent().plus(MakePhotoponModule(this)).inject(this)

    val viewGroup = inflater.inflate(R.layout.fragment_make_photopon, container, false) as ViewGroup

    viewGroup.fab_take_photo.setOnClickListener { onShutterButtonClicked() }
    textureView = viewGroup.camera

    presenter.onCreate()

    return viewGroup
  }

  override fun displayCoupons(coupons: ArrayList<GiftViewModel>) {


    Log.i(TAG, "---------====---------")
    Log.i(TAG, "displayCoupons")
    Log.i(TAG, "---------====---------")

    view_pager.visibility = if (coupons.isEmpty()) View.GONE else View.VISIBLE

    viewModels = coupons

    val adapter = MakeViewPagerAdapter(context!!, coupons)
    view_pager.adapter = adapter
    view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
      override fun onPageScrollStateChanged(state: Int) = Unit
      override fun onPageScrolled(position: Int, positionOffset: Float,
                                  positionOffsetPixels: Int) = Unit

      override fun onPageSelected(position: Int) {

        Log.i(TAG, "---------====---------")
        Log.i(TAG, "override fun onPageSelected(position: Int) { position = $position")
        Log.i(TAG, "---------====---------")

        selectedPage = position
      }
    })

    arguments?.get(ARGUMENT_PRESELECTED_COUPON)?.let {coupon ->

      Log.i(TAG, "---------====---------")
      Log.i(TAG, "displayCoupons  arguments?.get(ARGUMENT_PRESELECTED_COUPON)?.let {coupon ->")
      Log.i(TAG, "---------====---------")
      Log.i(TAG, "---------====---------")
      Log.i(TAG, "---------====---------")
      Log.i(TAG, "coupon = $coupon")

      coupon as GiftViewModel

      Log.i(TAG, "---------====---------")
      Log.i(TAG, "coupon as GiftViewModel")
      Log.i(TAG, "---------====---------")
      Log.i(TAG, "coupon = $coupon")
      Log.i(TAG, "---------====---------")

      view_pager.currentItem = coupons.map { it.parseObject.objectId }.indexOf(coupon.parseObject.objectId)

    }
  }

  /**
   * [TextureView.SurfaceTextureListener] handles several lifecycle events on a
   * [TextureView].
   */
  private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {

    override fun onSurfaceTextureAvailable(texture: SurfaceTexture, width: Int, height: Int) {

      Log.i(TAG, "---------====---------")
      Log.i(TAG, "surfaceTextureListener  onSurfaceTextureAvailable")
      Log.i(TAG, "---------====---------")

      openCamera(width, height)
    }

    override fun onSurfaceTextureSizeChanged(texture: SurfaceTexture, width: Int, height: Int) {

      Log.i(TAG, "---------====---------")
      Log.i(TAG, "surfaceTextureListener  onSurfaceTextureSizeChanged")
      Log.i(TAG, "---------====---------")

      configureTransform(width, height)
    }

    override fun onSurfaceTextureDestroyed(texture: SurfaceTexture) = true

    override fun onSurfaceTextureUpdated(texture: SurfaceTexture) = Unit

  }

  /**
   * ID of the current [CameraDevice].
   */
  private lateinit var cameraId: String

  /**
   * An [AutoFitTextureView] for camera preview.
   */
  private lateinit var textureView: AutoFitTextureView

  /**
   * A [CameraCaptureSession] for camera preview.
   */
  private var captureSession: CameraCaptureSession? = null

  /**
   * A reference to the opened [CameraDevice].
   */
  private var cameraDevice: CameraDevice? = null

  /**
   * The [android.util.Size] of camera preview.
   */
  private lateinit var previewSize: Size

  /**
   * [CameraDevice.StateCallback] is called when [CameraDevice] changes its state.
   */
  private val stateCallback = object : CameraDevice.StateCallback() {

    override fun onOpened(cameraDevice: CameraDevice) {

      Log.i(TAG, "---------====---------")
      Log.i(TAG, "stateCallback onOpened")
      Log.i(TAG, "---------====---------")

      cameraOpenCloseLock.release()
      this@MakePhotoponFragment.cameraDevice = cameraDevice
      createCameraPreviewSession()
    }

    override fun onDisconnected(cameraDevice: CameraDevice) {

      Log.i(TAG, "---------====---------")
      Log.i(TAG, "stateCallback onDisconnected")
      Log.i(TAG, "---------====---------")

      cameraOpenCloseLock.release()
      cameraDevice.close()
      this@MakePhotoponFragment.cameraDevice = null
    }

    override fun onError(cameraDevice: CameraDevice, error: Int) {

      Log.i(TAG, "---------====---------")
      Log.i(TAG, "stateCallback onError  error = $error")
      Log.i(TAG, "stateCallback onError  cameraDevice = $cameraDevice")
      Log.i(TAG, "---------====---------")

      onDisconnected(cameraDevice)
      callback?.onCameraError()
    }

  }

  /**
   * An additional thread for running tasks that shouldn't block the UI.
   */
  private var backgroundThread: HandlerThread? = null

  /**
   * A [Handler] for running tasks in the background.
   */
  private var backgroundHandler: Handler? = null

  /**
   * An [ImageReader] that handles still image capture.
   */
  private var imageReader: ImageReader? = null

  /**
   * This is the output file for our picture.
   */
  private lateinit var file: File

  /**
   * This a callback object for the [ImageReader]. "onImageAvailable" will be called when a
   * still image is ready to be saved.
   */
  private val onImageAvailableListener = ImageReader.OnImageAvailableListener {

    Log.i(TAG, "---------====---------")
    Log.i(TAG, "onImageAvailableListener")
    Log.i(TAG, "---------====---------")

    backgroundHandler?.post(ImageSaver(it.acquireNextImage(), file))
  }

  /**
   * [CaptureRequest.Builder] for the camera preview
   */
  private lateinit var previewRequestBuilder: CaptureRequest.Builder

  /**
   * [CaptureRequest] generated by [.previewRequestBuilder]
   */
  private lateinit var previewRequest: CaptureRequest

  /**
   * The current state of camera state for taking pictures.
   *
   * @see .captureCallback
   */
  private var state = STATE_PREVIEW

  /**
   * A [Semaphore] to prevent the app from exiting before closing the camera.
   */
  private val cameraOpenCloseLock = Semaphore(1)

  /**
   * Whether the current camera device supports Flash or not.
   */
  private var flashSupported = false

  /**
   * Orientation of the camera sensor
   */
  private var sensorOrientation = 0

  /**
   * A [CameraCaptureSession.CaptureCallback] that handles events related to JPEG capture.
   */
  private val captureCallback = object : CameraCaptureSession.CaptureCallback() {

    private fun process(result: CaptureResult) {

      when (state) {
        STATE_PREVIEW -> Unit // Do nothing when the camera preview is working normally.
        STATE_WAITING_LOCK -> capturePicture(result)
        STATE_WAITING_PRECAPTURE -> {
          // CONTROL_AE_STATE can be null on some devices
          val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
          if (aeState == null ||
              aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
              aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
            state = STATE_WAITING_NON_PRECAPTURE
          }
        }
        STATE_WAITING_NON_PRECAPTURE -> {
          // CONTROL_AE_STATE can be null on some devices
          val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
          if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
            state = STATE_PICTURE_TAKEN
            captureStillPicture()
          }
        }
      }
    }

    private fun capturePicture(result: CaptureResult) {

      captureStillPicture()
//      val afState = result.get(CaptureResult.CONTROL_AF_STATE)
//      if (afState == null) {
//        captureStillPicture()
//      } else if (afState == CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED
//          || afState == CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED) {
//        // CONTROL_AE_STATE can be null on some devices
//        val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
//        if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
//          state = STATE_PICTURE_TAKEN
//          captureStillPicture()
//        } else {
//          runPrecaptureSequence()
//        }
//      }
    }

    override fun onCaptureProgressed(session: CameraCaptureSession,
                                     request: CaptureRequest,
                                     partialResult: CaptureResult) {

      process(partialResult)
    }

    override fun onCaptureCompleted(session: CameraCaptureSession,
                                    request: CaptureRequest,
                                    result: TotalCaptureResult) {

      process(result)
    }

  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {

    Log.i(TAG, "---------====---------")
    Log.i(TAG, "onActivityCreated")
    Log.i(TAG, "---------====---------")

    super.onActivityCreated(savedInstanceState)
    file = File(activity?.getExternalFilesDir(null),
        PIC_FILE_NAME)
  }

  override fun onResume() {

    Log.i(TAG, "---------====---------")
    Log.i(TAG, "onResume")
    Log.i(TAG, "---------====---------")

    super.onResume()
    startBackgroundThread()

    // When the screen is turned off and turned back on, the SurfaceTexture is already
    // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
    // a camera and start preview from here (otherwise, we wait until the surface is ready in
    // the SurfaceTextureListener).
    if (textureView.isAvailable) {
      openCamera(textureView.width, textureView.height)
    } else {
      textureView.surfaceTextureListener = surfaceTextureListener
    }
  }

  override fun onPause() {

    Log.i(TAG, "---------====---------")
    Log.i(TAG, "onPause")
    Log.i(TAG, "---------====---------")

    closeCamera()
    stopBackgroundThread()
    super.onPause()
  }

  /**
   * Sets up member variables related to camera.
   *
   * @param width  The width of available size for camera preview
   * @param height The height of available size for camera preview
   */
  private fun setUpCameraOutputs(width: Int, height: Int) {

    Log.i(TAG, "---------====---------")
    Log.i(TAG, "setUpCameraOutputs")
    Log.i(TAG, "---------====---------")

    val manager = activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    try {
      for (cameraId in manager.cameraIdList) {

        Log.i(TAG, "---------====---------")
        Log.i(TAG, "for (cameraId in manager.cameraIdList) {")
        Log.i(TAG, "cameraId = $cameraId")
        Log.i(TAG, "---------====---------")

        val characteristics = manager.getCameraCharacteristics(cameraId)

        // We don't use a front facing camera in this sample.
        val cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING)
        if (cameraDirection != null &&
            cameraDirection == CameraCharacteristics.LENS_FACING_FRONT) {
          continue
        }

        val map = characteristics.get(
            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP) ?: continue

        // For still image captures, we use the largest available size.
        val largest = Collections.max(
            Arrays.asList(*map.getOutputSizes(ImageFormat.JPEG)),
            CompareSizesByArea())
        imageReader = ImageReader.newInstance(largest.width, largest.height,
            ImageFormat.JPEG, /*maxImages*/ 2).apply {
          setOnImageAvailableListener(onImageAvailableListener, backgroundHandler)
        }

        // Find out if we need to swap dimension to get the preview size relative to sensor
        // coordinate.
        val displayRotation = activity?.windowManager?.defaultDisplay?.rotation

        sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)
        val swappedDimensions = areDimensionsSwapped(displayRotation!!)

        val displaySize = Point()
        activity?.windowManager?.defaultDisplay?.getSize(displaySize)
        val rotatedPreviewWidth = if (swappedDimensions) height else width
        val rotatedPreviewHeight = if (swappedDimensions) width else height
        var maxPreviewWidth = if (swappedDimensions) displaySize.y else displaySize.x
        var maxPreviewHeight = if (swappedDimensions) displaySize.x else displaySize.y

        if (maxPreviewWidth > MAX_PREVIEW_WIDTH) maxPreviewWidth = MAX_PREVIEW_WIDTH
        if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) maxPreviewHeight = MAX_PREVIEW_HEIGHT

        // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
        // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
        // garbage capture data.
        previewSize = chooseOptimalSize(
            map.getOutputSizes(SurfaceTexture::class.java),
            rotatedPreviewWidth, rotatedPreviewHeight,
            maxPreviewWidth, maxPreviewHeight,
            largest)
//
//        // We fit the aspect ratio of TextureView to the size of preview we picked.
//        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//          textureView.setAspectRatio(previewSize.width, previewSize.height)
//        } else {
//          textureView.setAspectRatio(previewSize.height, previewSize.width)
//        }

        // Check if the flash is supported.
        flashSupported =
            characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true

        this.cameraId = cameraId

        // We've found a viable camera and finished setting up member variables,
        // so we don't need to iterate through other available cameras.
        return
      }
    } catch (e: CameraAccessException) {
      Log.e(TAG, e.toString())
    } catch (e: NullPointerException) {
      Log.e(TAG, e.toString())
      // Currently an NPE is thrown when the Camera2API is used but not supported on the
      // device this code runs.
      callback?.onCameraError()
    }

  }

  /**
   * Determines if the dimensions are swapped given the phone's current rotation.
   *
   * @param displayRotation The current rotation of the display
   *
   * @return true if the dimensions are swapped, false otherwise.
   */
  private fun areDimensionsSwapped(displayRotation: Int): Boolean {

    Log.i(TAG, "---------====---------")
    Log.i(TAG, "areDimensionsSwapped")
    Log.i(TAG, "---------====---------")

    var swappedDimensions = false
    when (displayRotation) {
      Surface.ROTATION_0, Surface.ROTATION_180 -> {
        if (sensorOrientation == 90 || sensorOrientation == 270) {
          swappedDimensions = true
        }
      }
      Surface.ROTATION_90, Surface.ROTATION_270 -> {
        if (sensorOrientation == 0 || sensorOrientation == 180) {
          swappedDimensions = true
        }
      }
      else -> {
        Log.e(TAG, "Display rotation is invalid: $displayRotation")
      }
    }
    return swappedDimensions
  }


  /**
   * Opens the camera specified by [cameraId].
   */
  @SuppressLint("MissingPermission")
  private fun openCamera(width: Int, height: Int) {

    Log.i(TAG, "---------====---------")
    Log.i(TAG, "openCamera")
    Log.i(TAG, "---------====---------")

    setUpCameraOutputs(width, height)
    configureTransform(width, height)
    val manager = activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    try {
      // Wait for camera to open - 2.5 seconds is sufficient
      if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
        throw RuntimeException("Time out waiting to lock camera opening.")
      }
      manager.openCamera(cameraId, stateCallback, backgroundHandler)
    } catch (e: CameraAccessException) {
      Log.e(TAG, e.toString())
    } catch (e: InterruptedException) {
      throw RuntimeException("Interrupted while trying to lock camera opening.", e)
    }
  }

  /**
   * Closes the current [CameraDevice].
   */
  private fun closeCamera() {

    Log.i(TAG, "---------====---------")
    Log.i(TAG, "closeCamera")
    Log.i(TAG, "---------====---------")

    try {

      Log.i(TAG, "---------====---------")
      Log.i(TAG, "closeCamera   try {")
      Log.i(TAG, "---------====---------")

      cameraOpenCloseLock.acquire()
      captureSession?.close()
      captureSession = null
      cameraDevice?.close()
      cameraDevice = null
      imageReader?.close()
      imageReader = null
    } catch (e: InterruptedException) {

      Log.i(TAG, "---------====---------")
      Log.i(TAG, "closeCamera   catch {   e:")
      Log.e(TAG, e.toString())
      Log.i(TAG, "---------====---------")

      throw RuntimeException("Interrupted while trying to lock camera closing.", e)
    } finally {

      Log.i(TAG, "---------====---------")
      Log.i(TAG, "closeCamera   finally {")
      Log.i(TAG, "---------====---------")

      cameraOpenCloseLock.release()
    }
  }

  /**
   * Starts a background thread and its [Handler].
   */
  private fun startBackgroundThread() {

    Log.i(TAG, "---------====---------")
    Log.i(TAG, "setUpCameraOutputs")
    Log.i(TAG, "---------====---------")

    backgroundThread = HandlerThread("CameraBackground").also { it.start() }
    backgroundHandler = Handler(backgroundThread?.looper)
  }

  /**
   * Stops the background thread and its [Handler].
   */
  private fun stopBackgroundThread() {

    Log.i(TAG, "---------====---------")
    Log.i(TAG, "stopBackgroundThread")
    Log.i(TAG, "---------====---------")

    backgroundThread?.quitSafely()
    try {
      backgroundThread?.join()
      backgroundThread = null
      backgroundHandler = null
    } catch (e: InterruptedException) {
      Log.e(TAG, e.toString())
    }
  }

  /**
   * Creates a new [CameraCaptureSession] for camera preview.
   */
  private fun createCameraPreviewSession() {

    Log.i(TAG, "---------====---------")
    Log.i(TAG, "createCameraPreviewSession")
    Log.i(TAG, "---------====---------")

    try {

      Log.i(TAG, "----------------")
      Log.i(TAG, "createCameraPreviewSession  try {")
      Log.i(TAG, "----------------")

      val texture = textureView.surfaceTexture

      // We configure the size of default buffer to be the size of camera preview we want.
      texture.setDefaultBufferSize(previewSize.width, previewSize.height)

      // This is the output Surface we need to start preview.
      val surface = Surface(texture)

      // We set up a CaptureRequest.Builder with the output Surface.
      previewRequestBuilder = cameraDevice!!.createCaptureRequest(
          CameraDevice.TEMPLATE_PREVIEW
      )
      previewRequestBuilder.addTarget(surface)


      Log.i(TAG, "----------------")
      Log.i(TAG, "----------------")
      Log.i(TAG, "createCameraPreviewSession  try {   previewRequestBuilder.addTarget(surface)")
      Log.i(TAG, "----------------")
      Log.i(TAG, "----------------")

      // Here, we create a CameraCaptureSession for camera preview.
      cameraDevice?.createCaptureSession(Arrays.asList(surface, imageReader?.surface),
          object : CameraCaptureSession.StateCallback() {

            override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {

              Log.i(TAG, "----------------")
              Log.i(TAG, "----------------")
              Log.i(TAG, "object : CameraCaptureSession.StateCallback() {")
              Log.i(TAG, "override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {")
              Log.i(TAG, "----------------")
              Log.i(TAG, "----------------")

              // The camera is already closed
              if (cameraDevice == null){

                Log.i(TAG, "----------------")
                Log.i(TAG, "----------------")
                Log.i(TAG, "if(cameraDevice == null")
                Log.i(TAG, "----------------")
                Log.i(TAG, "----------------")
                return
              }

              // When the session is ready, we start displaying the preview.
              captureSession = cameraCaptureSession

              Log.i(TAG, "----------------")
              Log.i(TAG, "captureSession = cameraCaptureSession")
              Log.i(TAG, "----------------")

              try {
                Log.i(TAG, "----------------")
                Log.i(TAG, "----------------")
                Log.i(TAG, "override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {")
                Log.i(TAG, "...")
                Log.i(TAG, "try { ")
                Log.i(TAG, "----------------")
                Log.i(TAG, "----------------")

                // Auto focus should be continuous for camera preview.
                previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                // Flash is automatically enabled when necessary.
                setAutoFlash(previewRequestBuilder)

                // Finally, we start displaying the camera preview.
                previewRequest = previewRequestBuilder.build()
                captureSession?.setRepeatingRequest(previewRequest,
                    captureCallback, backgroundHandler)



              } catch (e: CameraAccessException) {

                Log.i(TAG, "----------------")
                Log.i(TAG, "----------------")
                Log.i(TAG, "} catch (e: CameraAccessException")
                Log.i(TAG, "cameraDevice?.createCaptureSession(Arrays.asList(surface, imageReader?.surface),\n" +
                        "          object : CameraCaptureSession.StateCallback() {" +
                        "...    } catch (e: CameraAccessException) {")
                Log.i(TAG, "----------------")
                Log.i(TAG, "----------------")
                Log.e(TAG, e.toString())
                Log.i(TAG, "----------------")
                Log.i(TAG, "----------------")
              }
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {

              Log.i(TAG, "----------------")
              Log.i(TAG, "override fun onConfigureFailed(session: CameraCaptureSession) {")
              Log.i(TAG, "----------------")

              callback?.onCameraError()
            }
          }, null)
    } catch (e: CameraAccessException) {

      Log.i(TAG, "----------------")
      Log.i(TAG, "----------------")
      Log.i(TAG, "} catch (e: CameraAccessException")
      Log.i(TAG, "----------------")
      Log.i(TAG, "----------------")
      Log.i(TAG, "ERROR INFO: ")

      Log.e(TAG, e.toString())
      Log.i(TAG, "----------------")
      Log.i(TAG, "----------------")
    }
  }

  /**
   * Configures the necessary [android.graphics.Matrix] transformation to `textureView`.
   * This method should be called after the camera preview size is determined in
   * setUpCameraOutputs and also the size of `textureView` is fixed.
   *
   * @param viewWidth  The width of `textureView`
   * @param viewHeight The height of `textureView`
   */
  private fun configureTransform(viewWidth: Int, viewHeight: Int) {

    Log.i(TAG, "----------------")
    Log.i(TAG, "private fun configureTransform(viewWidth: Int, viewHeight: Int) {")
    Log.i(TAG, "----------------")

    activity ?: return

    Log.i(TAG, "----------------")
    Log.i(TAG, "activity ?: return")
    Log.i(TAG, "activity = $activity")
    Log.i(TAG, "----------------")

    val rotation = activity?.windowManager?.defaultDisplay?.rotation
    val matrix = Matrix()
    val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
    val bufferRect = RectF(0f, 0f, previewSize.height.toFloat(), previewSize.width.toFloat())
    val centerX = viewRect.centerX()
    val centerY = viewRect.centerY()

    if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {

      Log.i(TAG, "----------------")
      Log.i(TAG, "if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {")
      Log.i(TAG, "----------------")

      bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
      val scale = Math.max(
          viewHeight.toFloat() / previewSize.height,
          viewWidth.toFloat() / previewSize.width)
      with(matrix) {
        setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
        postScale(scale, scale, centerX, centerY)
        postRotate((90 * (rotation - 2)).toFloat(), centerX, centerY)
      }
    } else if (Surface.ROTATION_180 == rotation) {

      Log.i(TAG, "----------------")
      Log.i(TAG, "} else if (Surface.ROTATION_180 == rotation) {")
      Log.i(TAG, "----------------")

      matrix.postRotate(180f, centerX, centerY)
    }

    Log.i(TAG, "----------------")
    Log.i(TAG, "END   if () else if ()...")
    Log.i(TAG, "----------------")

    textureView.setTransform(matrix)

    Log.i(TAG, "----------------")
    Log.i(TAG, "textureView.setTransform(matrix)")
    Log.i(TAG, "----------------")

    Log.i(TAG, "----------------")
    Log.i(TAG, "configureTransform    END")
    Log.i(TAG, "----------------")
    Log.i(TAG, "----------------")

  }

  /**
   * Lock the focus as the first step for a still image capture.
   */
  private fun lockFocus() {

    Log.i(TAG, "--------------")
    Log.i(TAG, "private fun lockFocus()")
    Log.i(TAG, "--------------")

    try {
      Log.i(TAG, "--------------")
      Log.i(TAG, "try {")
      Log.i(TAG, "--------------")
      // This is how to tell the camera to lock focus.
      previewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
          CameraMetadata.CONTROL_AF_TRIGGER_START)
      // Tell #captureCallback to wait for the lock.
      state = STATE_WAITING_LOCK
      captureSession?.capture(previewRequestBuilder.build(), captureCallback,
          backgroundHandler)
    } catch (e: CameraAccessException) {

      Log.i(TAG, "--------------")
      Log.i(TAG, "CameraAccessException")
      Log.i(TAG, "----------------")
      Log.e(TAG, e.toString())
      Log.i(TAG, "----------------")
      Log.i(TAG, "----------------")
      Log.i(TAG, "----------------")
    }

  }

  /**
   * Run the precapture sequence for capturing a still image. This method should be called when
   * we get a response in [.captureCallback] from [.lockFocus].
   */
  private fun runPrecaptureSequence() {

    Log.i(TAG, "--------------")
    Log.i(TAG, "runPrecaptureSequence")
    Log.i(TAG, "--------------")

    try {

      Log.i(TAG, "--------------")
      Log.i(TAG, "runPrecaptureSequence :: try { ")
      Log.i(TAG, "--------------")

      // This is how to tell the camera to trigger.
      previewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
          CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START)
      // Tell #captureCallback to wait for the precapture sequence to be set.
      state = STATE_WAITING_PRECAPTURE
      captureSession?.capture(previewRequestBuilder.build(), captureCallback,
          backgroundHandler)
    } catch (e: CameraAccessException) {
      Log.i(TAG, "--------------")
      Log.i(TAG, "--------------")
      Log.i(TAG, "runPrecaptureSequence :: try {} catch (e: CameraAccessException")
      Log.i(TAG, "--------------")
      Log.i(TAG, "ERROR   e.toString():")
      Log.e(TAG, e.toString())
      Log.i(TAG, "--------------")
      Log.i(TAG, "--------------")
    }

    Log.i(TAG, "--------------")
    Log.i(TAG, "END   try{...} catch (e: CameraAccessException) {...}")
    Log.i(TAG, "--------------")
    Log.i(TAG, "--------------")
  }

  /**
   * Capture a still picture. This method should be called when we get a response in
   * [.captureCallback] from both [.lockFocus].
   */
  private fun captureStillPicture() {

    Log.i(TAG, "--------------")
    Log.i(TAG, "captureStillPicture")
    Log.i(TAG, "--------------")

    try {

      Log.i(TAG, "--------------")
      Log.i(TAG, "    try {")
      Log.i(TAG, "--------------")

      if (activity == null || cameraDevice == null) return
      val rotation = activity?.windowManager?.defaultDisplay?.rotation


      Log.i(TAG, "--------------")
      Log.i(TAG, "rotation = activity?.windowManager?...")
      Log.i(TAG, "--------------")

      // This is the CaptureRequest.Builder that we use to take a picture.
      val captureBuilder = cameraDevice?.createCaptureRequest(
          CameraDevice.TEMPLATE_STILL_CAPTURE)?.apply {
        addTarget(imageReader?.surface)

        // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
        // We have to take that into account and rotate JPEG properly.
        // For devices with orientation of 90, we return our mapping from ORIENTATIONS.
        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
        set(CaptureRequest.JPEG_ORIENTATION,
            (ORIENTATIONS.get(rotation!!) + sensorOrientation + 270) % 360)

        // Use the same AE and AF modes as the preview.
        set(CaptureRequest.CONTROL_AF_MODE,
            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
      }?.also { setAutoFlash(it) }

      Log.i(TAG, "----------------")
      Log.i(TAG, "captureBuilderRequest{ ... }?.also { setAutoFlash(it) }")
      Log.i(TAG, "----------------")

      val captureCallback = object : CameraCaptureSession.CaptureCallback() {

        override fun onCaptureCompleted(session: CameraCaptureSession,
                                        request: CaptureRequest,
                                        result: TotalCaptureResult) {

          callback?.onPictureTaken(file, viewModels?.get(selectedPage))

          Log.d(TAG, file.toString())

          unlockFocus()

        }
      }

      captureSession?.apply {
        stopRepeating()
        abortCaptures()
        capture(captureBuilder?.build(), captureCallback, null)
      }
    } catch (e: CameraAccessException) {

      Log.e(TAG, e.toString())

    }

  }

  /**
   * Unlock the focus. This method should be called when still image capture sequence is
   * finished.
   */
  private fun unlockFocus() {

    Log.i(TAG, "----------------")
    Log.i(TAG, "----------------")
    Log.i(TAG, "unlockFocus")
    Log.i(TAG, "----------------")

    try {

      Log.i(TAG, "----------------")
      Log.i(TAG, "----------------")
      Log.i(TAG, "unlockFocus   try {")

      // Reset the auto-focus trigger
      previewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
          CameraMetadata.CONTROL_AF_TRIGGER_CANCEL)
      setAutoFlash(previewRequestBuilder)
      captureSession?.capture(previewRequestBuilder.build(), captureCallback,
          backgroundHandler)
      // After this, the camera will go back to the normal state of preview.
      state = STATE_PREVIEW
      captureSession?.setRepeatingRequest(previewRequest, captureCallback,
          backgroundHandler)
    } catch (e: CameraAccessException) {

      Log.i(TAG, "----------------")
      Log.i(TAG, "unlockFocus   try { catch (e: CameraAccessException")
      Log.i(TAG, "----------------")
      Log.i(TAG, "----------------")
      Log.e(TAG, e.toString())
      Log.i(TAG, "----------------")
      Log.i(TAG, "----------------")
      Log.i(TAG, "----------------")
    }

  }

  private fun onShutterButtonClicked() {

    Log.i(TAG, "----------------")
    Log.i(TAG, "----------------")
    Log.i(TAG, "onShutterButtonClicked")
    Log.i(TAG, "----------------")

    lockFocus()
  }

  private fun setAutoFlash(requestBuilder: CaptureRequest.Builder) {

    Log.i(TAG, "----------------")
    Log.i(TAG, "----------------")
    Log.i(TAG, "----------------")

    if (flashSupported) {

      Log.i(TAG, "----------------")
      Log.i(TAG, "setAutoFlash :: if(flashSupported")
      Log.i(TAG, "----------------")

      requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
          CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)

      Log.i(TAG, "----------------")
      Log.i(TAG, "requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,\n" +
              "          CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)")
      Log.i(TAG, "----------------")

      Log.i(TAG, "----------------")
      Log.i(TAG, "setAutoFlash :: END IF [if(flashSupported]")
      Log.i(TAG, "----------------")

    }

    Log.i(TAG, "----------------")
    Log.i(TAG, "END setAutoFlash")
    Log.i(TAG, "----------------")
  }

  companion object {
    /**
     * Given `choices` of `Size`s supported by a camera, choose the smallest one that
     * is at least as large as the respective texture view size, and that is at most as large as
     * the respective max size, and whose aspect ratio matches with the specified value. If such
     * size doesn't exist, choose the largest one that is at most as large as the respective max
     * size, and whose aspect ratio matches with the specified value.
     *
     * @param choices           The list of sizes that the camera supports for the intended
     *                          output class
     * @param textureViewWidth  The width of the texture view relative to sensor coordinate
     * @param textureViewHeight The height of the texture view relative to sensor coordinate
     * @param maxWidth          The maximum width that can be chosen
     * @param maxHeight         The maximum height that can be chosen
     * @param aspectRatio       The aspect ratio
     * @return The optimal `Size`, or an arbitrary one if none were big enough
     */
    @JvmStatic
    private fun chooseOptimalSize(
        choices: Array<Size>,
        textureViewWidth: Int,
        textureViewHeight: Int,
        maxWidth: Int,
        maxHeight: Int,
        aspectRatio: Size
    ): Size {

      // Collect the supported resolutions that are at least as big as the preview Surface
      val bigEnough = ArrayList<Size>()
      // Collect the supported resolutions that are smaller than the preview Surface
      val notBigEnough = ArrayList<Size>()
      val w = aspectRatio.width
      val h = aspectRatio.height
      for (option in choices) {
        if (option.width <= maxWidth && option.height <= maxHeight &&
            option.height == option.width * h / w) {
          if (option.width >= textureViewWidth && option.height >= textureViewHeight) {
            bigEnough.add(option)
          } else {
            notBigEnough.add(option)
          }
        }
      }

      // Pick the smallest of those big enough. If there is no one big enough, pick the
      // largest of those not big enough.
      return when {
        bigEnough.size > 0 -> Collections.min(bigEnough, CompareSizesByArea())
        notBigEnough.size > 0 -> Collections.max(notBigEnough, CompareSizesByArea())
        else -> {
          Log.e(TAG, "Couldn't find any suitable preview size")
          choices[0]
        }
      }
    }


    /**
     * Conversion from screen rotation to JPEG orientation.
     */
    private val ORIENTATIONS = SparseIntArray()
    private const val PIC_FILE_NAME = "photopon.jpg"

    init {
      ORIENTATIONS.append(Surface.ROTATION_0, 90)
      ORIENTATIONS.append(Surface.ROTATION_90, 0)
      ORIENTATIONS.append(Surface.ROTATION_180, 270)
      ORIENTATIONS.append(Surface.ROTATION_270, 180)
    }

    /**
     * Tag for the [Log].
     */
    private const val TAG = "Camera2BasicFragment"

    /**
     * Camera state: Showing camera preview.
     */
    private const val STATE_PREVIEW = 0

    /**
     * Camera state: Waiting for the focus to be locked.
     */
    private const val STATE_WAITING_LOCK = 1

    /**
     * Camera state: Waiting for the exposure to be precapture state.
     */
    private const val STATE_WAITING_PRECAPTURE = 2

    /**
     * Camera state: Waiting for the exposure state to be something other than precapture.
     */
    private const val STATE_WAITING_NON_PRECAPTURE = 3

    /**
     * Camera state: Picture was taken.
     */
    private const val STATE_PICTURE_TAKEN = 4

    /**
     * Max preview width that is guaranteed by Camera2 API
     */
    private const val MAX_PREVIEW_WIDTH = 1920

    /**
     * Max preview height that is guaranteed by Camera2 API
     */
    private const val MAX_PREVIEW_HEIGHT = 1080
  }

}
