# MobileDiagnosticTool

Simple to use API for accessing RAM info, Battery Info and Camera functionality.

[![](https://jitpack.io/v/aakashvats2910/MobileDiagnosticTool.svg)](https://jitpack.io/#aakashvats2910/MobileDiagnosticTool)

<h2> Implementation</h2>
Add it in your root build.gradle at the end of repositories:

```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Add it in your project level build.gradle file:

```gradle
dependencies {
  implementation 'com.github.aakashvats2910:MobileDiagnosticTool:1.20'
}
```
<h2> Documentation </h2>
<h3> CameraInfo </h3>
In XML file of the activity

```xml
<TextureView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/textureView"/>
```
To open the read camera:
```java
CameraInfo cameraInfo = new CameraInfo(context, textureView);
cameraInfo.startRearCamera();
```

To open front camera:
```java
CameraInfo cameraInfo = new CameraInfo(context, textureView);
cameraInfo.startFrontCamera();
```

<h3> BatteryInfo </h3>

@return float, to get current percentage of battery.
```java
BatteryInfo.getBatteryPercentage(context);
```
@return boolean, to check if battery is charging or not
```java
BatteryInfo.isCharging(context);
```
@return boolean, to check if battery is charging over usb
```java
BatteryInfo.isChargingOverUSB(context);
```
@return boolean, to check if battery is charging over AC(charger)
```java
BatteryInfo.isChargingOverAC(context);
```

Get the updated values through abstract methods:
```java
BatteryInfo batteryInfo = new BatteryInfo(context) {
    @Override
    public void onBatteryHealthChanged(int healthCode, String batteryHealth) {

    }

    @Override
    public void onChargingStatusChanged(int statusCode, String chargingStatus) {

    }
};
```

<h3> RAMInfo </h3>

@return double, to get total RAM (in MB)
```java
RAMInfo.totalRAMInMB(context);
```
@return double, to get free RAM (in MB)
```java
RAMInfo.availableRAMInMB(context);
```
@return double, to get used RAM (in MB)
```java
RAMInfo.usedRAMInMB(context);
```

Get the updated value of RAM after specific periods of time:
```java
RAMInfo ramInfo = new RAMInfo(context) {
    @Override
    public void onRamInfoUpdated(double availableRAM, double usedRAM, double totalRAM) {

    }
};
```
<b><i>OR</i></b> use constructor with delay and period where<br/>
delay -> after how many millis to start execution.<br/>
period -> interval in executions.
```java
RAMInfo ramInfo = new RAMInfo(context, delay, period) {
    @Override
    public void onRamInfoUpdated(double availableRAM, double usedRAM, double totalRAM) {

    }
};

```
