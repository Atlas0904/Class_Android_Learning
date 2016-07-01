package com.as.atlas.teaorder;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.internal.LocationRequestUpdateData;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity
        implements GeoCodingTask.GeoCodingTaskResponse,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, RoutingListener, LocationListener {

    TextView textViewNote;
    TextView textViewMenuResult;
    TextView textViewStoreInfo;
    ImageView staticMap;

    MapFragment mapFragment;   // Use for Google Map
    GoogleMap googleMap;  // Google map

    GoogleApiClient googleApiClient;   //為了要拿到現在座標
    LocationRequest locationRequest;
    LatLng storeLocation;
    private ArrayList<Polyline> polylines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();
        String note = intent.getStringExtra("note");
        String menuResults = intent.getStringExtra("menuResults");
        String storeInfo = intent.getStringExtra("storeInfo");

        if (note != null) Log.d("debug", note);
        if (menuResults != null) Log.d("debug", menuResults);
        if (storeInfo != null) Log.d("debug", storeInfo);

        textViewNote = (TextView) findViewById(R.id.textViewNote);
        textViewMenuResult = (TextView) findViewById(R.id.textViewMenuResult);
        textViewStoreInfo = (TextView) findViewById(R.id.textViewStoreInfo);
        staticMap = (ImageView) findViewById(R.id.imageView);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment);

        if (note != null) {
            textViewNote.setText(note);
        }

        if (storeInfo != null) {
            textViewStoreInfo.setText(storeInfo);
        }

        String text = "";
        if (menuResults != null) {
            try {
                JSONArray jsonArray = new JSONArray(menuResults);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String mediumNumber = String.valueOf(obj.getInt(DrinkOrder.MEDIUM_CUP_NUM));
                    String largeMNumber = String.valueOf(obj.getInt(DrinkOrder.LARGE_CUP_NUM));

                    text += obj.getString(DrinkOrder.DRINK_NAME) + " :大杯" + largeMNumber + "杯" + " 中杯" + mediumNumber + "杯\n";
                    Log.d("Atlas", "text=" + text);
                }

            } catch (JSONException e) {
                Log.d("Atlas", "e:" + e.getMessage());
                e.printStackTrace();
            }
            textViewMenuResult.setText(text);
        }


        String[] storeInfos = storeInfo.split(",");
        if (storeInfos != null && storeInfos.length > 1) {   // 店員, 地址
            String addr = storeInfos[1];
            (new GeoCodingTask(this)).execute(addr);   // 因為  implement GeoCodingTaskResponse
        }


        // 回傳 Google map
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;

            }
        });

    }

    @Override
    public void responseWithGeoCodingResult(LatLng latLng) {
        if (googleMap != null) {
//            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17); // 相機的移動
//            //googleMap.moveCamera(cameraUpdate);
//            googleMap.animateCamera(cameraUpdate);  //  動畫移動

            googleMap.addMarker(new MarkerOptions().position(latLng));   // 紅色標誌
            storeLocation = latLng;
            createGoogleAPIClient();
        }
    }

    private void createGoogleAPIClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)   //哪個 activity跟他連
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)    // 直接用這個 activity去implement callback
                    .addApi(LocationServices.API)
                    .build();

            googleApiClient.connect();
        }
    }

    private void createLocationRequest() {    // 一直 update
        if (locationRequest == null) {
            locationRequest = new LocationRequest();
            locationRequest.setInterval(1000);
            //locationRequest.setFastestInterval();  其他 app 拿
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("Atlas", "onConnected");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("Atlas", "Permission not allow");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 23 以上才需要
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }

            return;
        }
        googleMap.setMyLocationEnabled(true);
        createLocationRequest();
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

        LatLng start = new LatLng(25.0186348, 121.5398379);

        if (location != null) {
            start = new LatLng(location.getLatitude(), location.getLongitude());   // 有可能 Geany 一開始給錯  導致沒有路線圖
            googleMap.addMarker(new MarkerOptions().position(start).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 17));


        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.WALKING)   // 指定路徑
                .waypoints(start, storeLocation)   // 起點終點
                .withListener(this)
                .build();

        routing.execute();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();    // start 時候再去連
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient !=  null) {
            googleApiClient.disconnect();
        }
    }


    // Routing start: withListener
    @Override
    public void onRoutingFailure(RouteException e) {
//        Log.d("Atlas", "onRoutingFailure() e:" + e.getMessage());    // 可能沒有寫好 所以 e.getMessage 是 null

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> routes, int shortestPathIndex) {  //可以很多組, 最短路線的 index

        // Copy from lesson page
        if (polylines != null) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < routes.size(); i++) {

            //In case of more than 5 alternative routes
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(Color.YELLOW);
            polyOptions.width(15 + i * 3);
            polyOptions.addAll(routes.get(i).getPoints());
            Polyline polyline = googleMap.addPolyline(polyOptions);
            polylines.add(polyline);
//            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ routes.get(i).getDistanceValue()+": duration - "+ routes.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }
        // Copy from lesson page

    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));

        //也可以 加自己的 Marker
    }
    // Routing end: withListener

    //    private static class GeoCodingTask extends AsyncTask<String, Void, Bitmap> {
//
//        WeakReference<ImageView> imageViewWeakReference;
//
//        public GeoCodingTask(ImageView imageView) {
//            this.imageViewWeakReference = new WeakReference<ImageView>(imageView);
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            String addr = params[0];
//            Log.d("Atlas", "doInBackground() addr:" + addr);
//            double[] latlng = Utils.getLatLngFromGoogleMapAPI(addr);
//            if (latlng != null) {
//                Log.d("Atlas", String.valueOf(latlng[0]));
//                Log.d("Atlas", String.valueOf(latlng[1]));
//            }
//            return Utils.getStaticMap(latlng);
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//            ImageView imageView = imageViewWeakReference.get();
//            imageView.setImageBitmap(bitmap);
//        }
//    }
}
