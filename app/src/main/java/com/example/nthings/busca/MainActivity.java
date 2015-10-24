package com.example.nthings.busca;

import android.*;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {
    String direccion="";
    GoogleMap mMap;
    double latitud, longitud;
    List<Double> linea=new ArrayList<Double>();
    DatabaseManager bd;
    boolean ir=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setUpMapIfNeeded();
        bd=new DatabaseManager(MainActivity.this);
        bd.borrar();
        bd.insertarRuta(24.0336112, -104.6384037,1);
        bd.insertarRuta(24.0741737,-104.5933568,1);
        final PolylineOptions POLILINEA = new PolylineOptions().width(4).color(MainActivity.this.getResources().getColor(R.color.orrange));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                final View prompt = li.inflate(R.layout.dialogo, null);
                AlertDialog.Builder Dialogo = new AlertDialog.Builder(MainActivity.this);
                Dialogo.setView(prompt);
                Dialogo
                        .setTitle("Destino")
                        .setCancelable(false)
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("BUScar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        direccion = ((EditText) prompt.findViewById(R.id.editText)).getText().toString();
                                        mMap.addMarker(new MarkerOptions()
                                                        .position(new LatLng(24.0558122,-104.6126951))
                                        );
                                        ir = true;
                                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
                                        builderSingle.setTitle("Estas Rutas Te Pueden Llevar A Tu Destino");

                                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                                MainActivity.this,
                                                android.R.layout.select_dialog_singlechoice);
                                        arrayAdapter.add("San Marcos");
                                        arrayAdapter.add("San Mateo");

                                        builderSingle.setNegativeButton(
                                                "Cancelar",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                        builderSingle.setAdapter(
                                                arrayAdapter,
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dibujarLinea(bd.sacarRuta(1));
                                                    }
                                                });
                                        builderSingle.show();
                                    }
                                }
                        );
                AlertDialog alerta = Dialogo.create();
                alerta.show();



            }
        });
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng latLng = new LatLng(24.0381619, -104.6330722);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                mMap.animateCamera(cameraUpdate);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng));
            }
        });
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ir==true) {
                    animar(dibujarLinea(bd.sacarRuta(1)),5,"San Marcos");
                    animar(dibujarLinea(bd.sacarRuta(1)),4,"San Mateo");
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Aún no nos dices a donde quieres ir", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(MainActivity.this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private Polyline dibujarLinea(PolylineOptions POLILINEA){
        Polyline linea=mMap.addPolyline(POLILINEA);
        return linea;
    }

    private void animar(final Polyline lineax,int por,String nombre){
        List<LatLng> puntos=lineax.getPoints();
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("naranja", "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, 52, 130, false);
        final Marker marker=mMap.addMarker(new MarkerOptions()
                        .position(puntos.get(0))
                        .title(nombre)
                        .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap))
        );
        LatLngInterpolator interpolator=new LatLngInterpolator.Linear();
        for(int x=1;x<puntos.size();x++) {
            MarkerAnimation.animateMarkerToGB(marker,puntos.get(x),interpolator,por);
        }
    }


    private void setUpMapIfNeeded() {
        // Configuramos el objeto GoogleMaps con valores iniciales.

        if (mMap == null) {
            //Instanciamos el objeto mMap a partir del MapFragment definido bajo el Id "map"
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            // Chequeamos si se ha obtenido correctamente una referencia al objeto GoogleMap
            if (mMap != null) {
                // El objeto GoogleMap ha sido referenciado correctamente
                //ahora podemos manipular sus propiedades

                //Seteamos el tipo de mapa
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                //Activamos la capa o layer MyLocation
                mMap.setMyLocationEnabled(true);

                // Getting LocationManager object from System Service LOCATION_SERVICE
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                // Creating a criteria object to retrieve provider
                Criteria criteria = new Criteria();

                // Getting the name of the best provider
                String provider = locationManager.getBestProvider(criteria, true);

                // Getting Current Location
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                }
                Location location = locationManager.getLastKnownLocation(provider);

                if (location != null) {
                    onLocationChanged(location);
                }
                locationManager.requestLocationUpdates(provider, 20000, 0, this);

            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        // Getting latitude of the current location
        latitud = location.getLatitude();

        // Getting longitude of the current location
        longitud = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitud, longitud);

        // Showing the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        Log.e("COORDENADAS", "Latitud: " + latitud + " Longitud: " + longitud);


    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
}
