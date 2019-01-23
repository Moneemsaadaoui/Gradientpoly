# Gradientpoly 🌈🌈🌈
Gradient polylines on android .. finally🤙⛳ 🌍

<img src="https://github.com/Moneemsaadaoui/Gradientpoly/blob/master/Screenshot_20190123-161621_GradientPolylinesExample.jpg" width="250">  <img src="https://github.com/Moneemsaadaoui/Gradientpoly/blob/master/Screenshot_20190123-161814_GradientPolylinesExample.jpg" width="250">  <img src="https://github.com/Moneemsaadaoui/Gradientpoly/blob/master/Screenshot_20190123-161841_GradientPolylinesExample.jpg" width="250">

## Getting started🦗
```gradle
compile 'com.rrdl.GradientPoly:GradientPoly:0.1.0'
```

## Usage 🐜
```java
    
    GradientPoly gradientPoly = new GradientPoly(mMap, MainActivity.this);
    
    gradientPoly.setApiKey("API KEY")
                        .setStartPoint(from).setEndPoint(to)
                        .setStartColor(Color.parseColor("#1eb5ab"))
                        .setWidth(11).setEndColor(Color.parseColor("#ff0098"))
                        .DrawPolyline();
```

## Customization 🐜

| method | Required? | Default | Type | Description |
|:---|:---:|:---|:---|:---|
| setApiKey | YES | "" | String | Google Maps API Key |
| setStartPoint | YES | null | LatLng | Path starting point |
| setEndPoint | YES | null | LatLng | Path finish point |
| setStartColor | YES | null | Int | Gradient start color |
| setEndColor | YES | null | Int | Gradient End color |
| setWidth | YES | null | Int | PolyLine Width in dp |


Pull requests are highly welcomed !
Go and make it better 👌👌



