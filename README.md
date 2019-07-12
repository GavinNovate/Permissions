# Permissions
[![Download](https://api.bintray.com/packages/novate/androidx/permissions/images/download.svg)](https://bintray.com/novate/androidx/permissions/_latestVersion) [![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu) | [简体中文](https://github.com/GavinNovate/Permissions/blob/androidx/README_CN.md) |

## How to use

### Dependencies

- androidx

  ```groovy
  implementation 'net.novate.androidx:permissions:1.0.0'
  ```

- android support

  ```groovy
  implementation 'net.novate.android:permissions:1.0.0'
  ```

### Take over callback

Add the following code in `BaseActivity` or each `Activity` to take over the callback of the `onRequestPermissionsResult` permission request.

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

### Example

#### Check permissions

Check if you have the specified permission. If you have all the permissions, it will return `true`, otherwise it will return `false`.

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

#### Request permissions

Request permission from the system to get the result of the request in a callback.

Result parameter description:

- Permissions.GRANTED  Permission is granted.
- Permissions.DENIED      Permission is denied.
- Permissions.IGNORE      Permission is denied, and the user chooses `Don't ask me again`。
- Permissions.CANCEL      Permission is canceled, usually canceled by the system when multiple requests are initiated at the same time.

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

#### Request all manifest permissions

Request all manifest permissions. This method is generally not recommended and may disrupt the user experience.

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
