package openuasl.surv.android;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;

public class MainActivity extends Activity {

	private final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter mBluetoothAdapter = null;
	private ImageButton fflash;
	private Camera mCamera = null;
	private int flag =1,fflag=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		
		fflash = (ImageButton) findViewById(R.id.flash_button);
		fflash.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(flag ==1){
				// TODO Auto-generated method stub
				mCamera = Camera.open();
				Camera.Parameters mCameraParameter = mCamera.getParameters();
				mCameraParameter.setFlashMode("torch");
				mCamera.setParameters(mCameraParameter);
				flag =2;
				}else{
					mCamera.release();
					flag =1;
				}
				if(fflag==1){
					fflash.setImageDrawable(getResources().getDrawable(R.drawable.lighton));
					fflag=2;
				}else{
					fflash.setImageDrawable(getResources().getDrawable(R.drawable.lightoff));
					fflag=1;
				}
				
			}
		});
		

	}
//	public void clickHandler(View v){
//				// TODO Auto-generated method stub
//				mCamera = Camera.open();
//				Camera.Parameters mCameraParameter = mCamera.getParameters();
//				mCameraParameter.setFlashMode("torch");
//				mCamera.setParameters(mCameraParameter);
//	}
	@Override
	protected void onPause() {
		if (mCamera != null) {
			mCamera.release();
		}
		super.onPause();
	}

	public void onStart() {
		super.onStart();

		if (!mBluetoothAdapter.isEnabled())
			mBluetoothAdapter.enable();
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntet = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

			discoverableIntet.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
			startActivity(discoverableIntet);
		}
	}

}
