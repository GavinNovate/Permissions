# Permissions
[![Download](https://api.bintray.com/packages/novate/android/permissions/images/download.svg)](https://bintray.com/novate/android/permissions/_latestVersion) [![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)

## 如何使用

### 添加依赖

- androidx

  ```groovy
  implementation 'net.novate.androidx:permissions:1.0.0'
  ```

- android support

  ```groovy
  implementation 'net.novate.android:permissions:1.0.0'
  ```

### 接管回调

在`BaseActivity`或每个`Activity`中添加如下代码来接管`onRequestPermissionsResult`权限请求的回调。

Kotlin

```kotlin
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        onRequestPermissionsResult(requestCode, grantResults)
    }
}
```

Java

```java
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        Permissions.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
```

### 使用示例

#### 判断权限

检查是否拥有指定权限，全部拥有返回`true`，否则返回`false`。

Kotlin

```kotlin
if (hasPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
    Log.d(TAG, "hasPermissions: WRITE_EXTERNAL_STORAGE")
}
```

Java

```java
if (Permissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
    Log.d(TAG, "hasPermissions: WRITE_EXTERNAL_STORAGE");
}
```

#### 请求权限

向系统请求权限，以回调的方式获取请求结果。

请求结果参数说明：

- Permissions.GRANTED  对应权限被授予。
- Permissions.DENIED      对应权限被拒绝。
- Permissions.IGNORE      对应权限被拒绝，且被用户选择不再提醒。
- Permissions.CANCEL      对应权限的请求被取消，通常会在同时发起多个请求时被系统取消。

Kotlin

```kotlin
requestPermissions(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
) { permissions, results ->
    if (results.all { it == Permissions.GRANTED }) {
        Log.d(TAG, "requestPermissions: Has all permissions: $permissions")
    }
    // GRANTED Permissions
    permissions.filterIndexed { index, _ -> results[index] == Permissions.GRANTED }
    // DENIED Permissions
    permissions.filterIndexed { index, _ -> results[index] == Permissions.DENIED }
    // IGNORE Permissions
    permissions.filterIndexed { index, _ -> results[index] == Permissions.IGNORE }
    // CANCEL Permissions
    permissions.filterIndexed { index, _ -> results[index] == Permissions.CANCEL }
}
```

Java

```java
Permissions.requestPermissions(this,
        new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE},
        (permissions, results) -> {
            boolean granted = true;
            for (int i = 0; i < permissions.length; i++) {
                granted &= results[i] == Permissions.GRANTED;
            }
            if (granted) {
                Log.d(TAG, "requestPermissions: Has all permissions: "
                        + Arrays.toString(permissions));
            }
        });
```

#### 请求全部权限

请求清单列表中列出的全部权限，通常不建议使用该方法，会破坏用户体验。

Kotlin

```kotlin
requestAllManifestPermissions { permissions, results ->
    if (results.all { it == Permissions.GRANTED }) {
        Log.d(TAG, "requestAllManifestPermissions: Has all permissions: $permissions")
    }
    // GRANTED Permissions
    permissions.filterIndexed { index, _ -> results[index] == Permissions.GRANTED }
    // DENIED Permissions
    permissions.filterIndexed { index, _ -> results[index] == Permissions.DENIED }
    // IGNORE Permissions
    permissions.filterIndexed { index, _ -> results[index] == Permissions.IGNORE }
    // CANCEL Permissions
    permissions.filterIndexed { index, _ -> results[index] == Permissions.CANCEL }
}
```

Java

```java
Permissions.requestAllManifestPermissions(this,
        (permissions, results) -> {
            boolean granted = true;
            for (int i = 0; i < permissions.length; i++) {
                granted &= results[i] == Permissions.GRANTED;
            }
            if (granted) {
                Log.d(TAG, "requestAllManifestPermissions: Has all permissions: "
                        + Arrays.toString(permissions));
            }
        });
```

