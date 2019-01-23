package com.rrdl.gradientpoly;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

public class GradientPoly {
    private GoogleMap mMap;
    private List<LatLng> route;
    private Activity mActivity;
    private Polyline line;
    private LatLng from, to;
    private int startColor;
    private int endColor;
    private int width = 15;
    private List<Polyline> oldPoly = new ArrayList<>();
    private int end_red = 100;
    private int end_green = 100;
    private int end_blue = 100;
    private int red = 0;
    private int green = 0;
    private int blue = 0;
    private String API_KEY;
    private float ZoomLevel;
    float redSteps;
    float greenSteps;
    float blueSteps;
    private List<PolylineOptions> gradientpoly = new ArrayList<>();
    private Thread make_poly_options;
    private List<LatLng> finalResult;

    public GradientPoly(GoogleMap map, Activity activity) {
        mMap = map;
        mActivity = activity;
    }


    public GradientPoly setStartPoint(LatLng from) {

        this.from = from;
        return this;
    }

    public GradientPoly setStartColor(int color) {
        this.startColor = color;
        return this;
    }

    public GradientPoly setWidth(int width) {
        this.width = width;
        return this;
    }

    public GradientPoly setEndColor(int color) {
        this.endColor = color;
        return this;
    }

    public GradientPoly setApiKey(String apiKey) {
        this.API_KEY = apiKey;
        return this;
    }

    public GradientPoly setEndPoint(LatLng to) {

        this.to = to;
        return this;
    }

    private List<LatLng> dichotomize(List<LatLng> input) {
        List<LatLng> result = new ArrayList<>();
        int i;
        int decalage = 0;
        result.add(input.get(0));
        result.add(new LatLng(input.get(0).latitude + ((input.get(0).latitude - input.get(1).latitude) / 2), input.get(0).longitude + ((input.get(0).longitude - input.get(1).longitude) / 2)));
        for (i = 1; i < input.size() - 1; i++) {
            result.add(new LatLng(result.get(i + decalage).latitude + ((input.get(i).latitude - result.get(i + decalage).latitude) / 2), result.get(i + decalage).longitude + ((input.get(i).longitude - result.get(i + decalage).longitude) / 2)));
            result.add(input.get(i));
            decalage++;
        }
        result.add(input.get(input.size() - 1));
        return result;
    }


    private void zoomRoute(List<LatLng> lstLatLngRoute) {
        if (mMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);
        int routePadding = 100;
        LatLngBounds latLngBounds = boundsBuilder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }

    public void DrawPolyline() {
        GoogleDirection.withServerKey(API_KEY)
                .from(from)
                .to(to)
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            Route directions = direction.getRouteList().get(0);
                            route = directions.getLegList().get(0).getDirectionPoint();
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            builder.include(from).include(to);
                            DrawPath(route);
                            zoomRoute(route);
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Toast.makeText(mActivity.getApplicationContext(), "Failed to find path", Toast.LENGTH_SHORT).show();
                        Log.e("FAILED TO FETCH PATH", "ARE YOU SURE YOU SET YOUR API KEY ?");
                    }
                });
    }

    private void DrawPath(final List<LatLng> decodedpoly) {
        end_red = getRed(endColor);
        end_green = getGreen(endColor);
        end_blue = getBlue(endColor);
        red = getRed(startColor);
        green = getGreen(startColor);
        blue = getBlue(startColor);
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (mMap.getCameraPosition().zoom != ZoomLevel) {


                    List<LatLng> result;
                    List<LatLng> simplified_list = PolyUtil.simplify(decodedpoly, 21 - mMap.getCameraPosition().zoom);
                    if (decodedpoly.size() > 300) {
                        result = PolyUtil.simplify(simplified_list, 200 - (mMap.getCameraPosition().zoom * 11));
                    } else if (decodedpoly.size() < 30) {
                        result = dichotomize(decodedpoly);
                    } else {
                        result = decodedpoly;
                    }
                    Log.e("Final result", result.size() + "");
                    finalResult = result;
                    redSteps = ((float) (end_red - red) / 255) / (float) finalResult.size();
                    greenSteps = ((float) (end_green - green) / 255) / (float) finalResult.size();
                    blueSteps = ((float) (end_blue - blue) / 255) / (float) finalResult.size();

//                    mMap.addMarker(new MarkerOptions().position(decodedpoly.get(decodedpoly.size() - 1)));
//                    mMap.addMarker(new MarkerOptions().position(decodedpoly.get(0)));


                    make_poly_options = new Thread() {
                        @Override
                        public void run() {
                            synchronized (this) {

                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                Log.e("size", finalResult.size() + " ");
                                builder.include(finalResult.get(0));
                                Log.e("steps", redSteps + " " + greenSteps + " " + blueSteps);

                                for (int i = 0; i < finalResult.size() - 1; i++) {
                                    builder.include(finalResult.get(i));
                                    Log.e("position", finalResult.get(i) + " ");
                                    float redColor = ((float) red / 255) + (redSteps * i);
                                    float greenColor = ((float) green / 255) + (greenSteps * i);
                                    float blueColor = ((float) blue / 255) + (blueSteps * i);
                                    Log.e("Color", "" + redColor);
                                    Log.e("steps", redSteps + " " + greenSteps + " " + blueSteps);
                                    int color = getIntFromColor(redColor, greenColor, blueColor);
                                    if (i == 0)
                                        Log.e("firstcol", redColor + " " + greenColor + " " + blueColor);
                                    if (i == finalResult.size() - 2)
                                        Log.e("lastcol", (int) redColor + " " + (int) greenColor + " " + (int) blueColor);
                                    gradientpoly.add(new PolylineOptions().width(width).color(color).geodesic(false).add(finalResult.get(i)).add(finalResult.get(i + 1)));
                                }

                                notifyAll();

                            }
                        }
                    };

                    make_poly_options.start();
                    synchronized (make_poly_options) {
                        try {

                            make_poly_options.wait();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Draw();
                    }
                }
                ZoomLevel = mMap.getCameraPosition().zoom;
            }

        });
    }

    private int getIntFromColor(float Red, float Green, float Blue) {
        int R = Math.round(255 * Red);
        int G = Math.round(255 * Green);
        int B = Math.round(255 * Blue);
        R = (R << 16) & 0x00FF0000;
        G = (G << 8) & 0x0000FF00;
        B = B & 0x000000FF;
        return 0xFF000000 | R | G | B;
    }

    private int getRed(int rgb) {
        return (rgb >> 16) & 0x0ff;
    }

    private int getGreen(int rgb) {
        return (rgb >> 8) & 0x0ff;
    }

    private int getBlue(int rgb) {
        return (rgb) & 0x0ff;
    }

    private void Draw() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//
                mMap.clear();
                for(Polyline poly:oldPoly){
                    poly.remove();
                }
                for (PolylineOptions po : gradientpoly) {
                    line = mMap.addPolyline(po);
                    line.setStartCap(new RoundCap());
                    line.setEndCap(new RoundCap());
                    oldPoly.add(line);
                }
                mMap.addMarker(new MarkerOptions().position(finalResult.get(finalResult.size() - 1)));
                mMap.addMarker(new MarkerOptions().position(finalResult.get(0)));


                gradientpoly.clear();


            }
        });
    }
}

