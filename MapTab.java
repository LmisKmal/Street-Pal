package com.sharekeg.streetpal.homefragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sharekeg.streetpal.Androidversionapi.ApiInterface;
import com.sharekeg.streetpal.R;
import com.sharekeg.streetpal.safeplace.SafePlaceActivity;
import com.sharekeg.streetpal.userinfoforlogin.CurrenLocation;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapTab extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = MapTab.class.getSimpleName();
    private static final int REQUEST_CHECK_SETTINGS = 20;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;
    private static final int RESULT_CANCELED = 14;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private OnFragmentInteractionListener mListener;
    private double lat;
    private double lon;
    private LocationRequest mLocationRequest;
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private ImageView ivCallForHelp, ivSearch;
    private Retrofit retrofit;
    private MapTab con = MapTab.this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);

        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_tab, container, false);
//        ivSearch = (ImageView) rootView.findViewById(R.id.ivSearch);
//        ivSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    Intent intent =
//                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
//                                    .build(getActivity());
//                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
//                } catch (Exception e) {
//                    // TODO: Handle the error.
//                }
//
//            }
//        });
        ivCallForHelp = (ImageView) rootView.findViewById(R.id.ivCallForHelp);
//        ivCallForHelp.setVisibility(View.INVISIBLE);
        ivCallForHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLocationViaSMS();
            }});
//                final AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
//                dialog.setTitle("Help Confirmation");
//                dialog.setCancelable(false);
//                dialog.setMessage("Your location will be send within 10s,If you want to cancel help click cancel.");
//                dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        dialog.dismiss();
//
//                    }
//                });
//                final AlertDialog alert = dialog.create();
//                alert.show();
//
//// opreation after some seconds
//                final Handler handler = new Handler();
//                final Runnable runnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        if (alert.isShowing()) {
//                            alert.dismiss();
////                            after 10s
//                            OpenSavePlace();
//                        }
//                    }
//                };
//
//                alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//                        handler.removeCallbacks(runnable);
//                    }
//                });
//
//                handler.postDelayed(runnable, 10000);
////                //   currenLocation(lat,long);
//
//            }
//        });
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        Log.d(TAG, "onCreateView");
        return rootView;
    }


    public void OpenSavePlace() {

        Intent intent = new Intent(getActivity(), SafePlaceActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        mMapView.onResume();
        setUpMap();

        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        mMapView.onPause();

        Log.d(TAG, "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();

        Log.d(TAG, "onLowMemory");
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION))
            return;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null)
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        else
            handleNewLocation(location);

        Log.d(TAG, "onConnected");
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        lat = location.getLatitude();
        lon = location.getLongitude();
        handleNewLocation(location);
    }

    Marker userMarker;

    private void handleNewLocation(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (userMarker == null) {
            userMarker = mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("I am here!"));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        } else {
            userMarker.setPosition(latLng);
        }


    }
    private void setUpMap() {
        if (mGoogleMap == null)
            mMapView.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mGoogleMap = googleMap;
                }
            });
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlaceAutocomplete.getPlace(getContext(), data);
//                showSearchedPlaceOnMap(place.getLatLng());
//
//            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                Status status = PlaceAutocomplete.getStatus(getContext(), data);
//            } else if (resultCode == RESULT_CANCELED) {
//            }
//        }
//    }





//    Marker placeMarker;
//
//    private void showSearchedPlaceOnMap(LatLng latLng) {
//        if (placeMarker == null) {
//            placeMarker = mGoogleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)));
//        } else {
//            placeMarker.setPosition(latLng);
//        }
//
//        if (mLastLocation != null) {
//            LatLng userLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//            GoogleMapsUtilities.getAndDrawPath(getContext(), mGoogleMap, userLatLng, latLng, false);
//        }
//
//
//    }

    //
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    private boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
    }


    private void currenLocation(double lat, double lon) {
        ApiInterface mApi = retrofit.create(ApiInterface.class);
        Call<CurrenLocation> mycall = mApi.SetLocation(new CurrenLocation(lat, lon));
        mycall.enqueue(new Callback<CurrenLocation>() {
            @Override
            public void onResponse(Call<CurrenLocation> call, Response<CurrenLocation> response) {

            }

            @Override
            public void onFailure(Call<CurrenLocation> call, Throwable t) {

            }
        });
    }
    public void sendLocationViaSMS(){
        String message="http://maps.google.com/?q=<" + lat+">,<"+ lon +">";
        String phoneNumber="01159139597";
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this.getActivity(), "Message Sent", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this.getActivity(),R.string.smthing_went_wrong,
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
