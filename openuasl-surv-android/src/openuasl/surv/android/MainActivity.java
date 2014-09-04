package openuasl.surv.android;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter mBluetoothAdapter = null;
	private ImageButton fflash;
	private Camera mCamera = null;
	private int flag =1,fflag=1;
	private BroadcastReceiver mBatteryBroadcastReceiver;
	private Context context;
	 private static final String tag = "BatteryChecker";
	 private static final String filetag = "<BatteryCheckMain> / ";
	 private TextView label;
	 Intent intent;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		context = this;
		  initBroadcastReceiver(context);
		  registerBatteryReceiver(context);
		  label=(TextView)findViewById(R.id.batteryy);
		 
		  
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		
		fflash = (ImageButton) findViewById(R.id.f);
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
					fflash.setImageDrawable(getResources().getDrawable(R.drawable.ef));
					fflag=2;
				}else{
					fflash.setImageDrawable(getResources().getDrawable(R.drawable.cd));
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

	 // make battery broadcast receiver
	 boolean initBroadcastReceiver(Context context)
	 {
	  mBatteryBroadcastReceiver = new BroadcastReceiver()
	  {

	   @Override
	   public void onReceive(Context context, Intent intent)
	   {
	    
	    final String action = intent.getAction();
	    
	    Log.i(tag, filetag + "mBatteryBroadcastReceiver - onReceive()/action:" +action);
	    
	    if(Intent.ACTION_BATTERY_CHANGED.equals(action))
	    {
	     Log.i(tag, filetag + "PLUG:" + intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
	       + " / STATUS:" + intent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN)
	       + " / LEVEL:" + intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0));
	     label.setText(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)+"%");
	    }
	   }

	   @Override
	   public IBinder peekService(Context myContext, Intent service)
	   {
	    // TODO Auto-generated method stub
	    return super.peekService(myContext, service);
	   }
	   
	  };
	  return true;
	 } //initBroadcastReceiver
	 
	 boolean registerBatteryReceiver(Context context)
	 {
	  Log.i(tag, filetag + "registerBatteryReceiver()");
	  
	  IntentFilter filter = new IntentFilter();
	  filter.addAction(Intent.ACTION_BATTERY_CHANGED);
	  filter.addAction(Intent.ACTION_BATTERY_LOW);
	  registerReceiver(mBatteryBroadcastReceiver, filter);
	  return true;
	 }
	}


