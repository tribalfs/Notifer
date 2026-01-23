# <img align="left" loading="lazy" src="readme-res/icon.png" height="50"/> Notifer

This app will transfer notifications from selected apps on your phone to your Http servers. The
servers will then receive a json containing information about the app and the notification like the
label, package name, title, message, progress (if available) and other useful information, as well
the app icon's dominant color. The idea behind this project is to light up RGB strips, when
receiving a message or a call, with the app icon's main color, but it can also be used for other
purposes.

### Features

- App selection and color override
- Multiple servers
- Messages can be encrypted with AES-GCM
- Notification content can be omitted for privacy
- OneUI design thanks to our [libraries](https://github.com/OneUIProject).
- When installed as a system app, it will also show up in the system notification settings.

### Python server example

A simple python server example for decrypting can be found [here](/server-example/python/main.py).

### JSON body format

#### Encrypted

```json
{
  "iv": "XK2+RgJm5MpNHq6e",
  "body": "aXmc....iz9N"
}
```

#### Non-encrypted / Decrypted

```json
{
  "color": {
    "hex": "#FF0000",
    "rgb": "[255, 0, 0]",
    "hsv": "[0.0, 1.0, 1.0]",
    "int": -65536
  },
  "id": 0,
  "time": 1769188787860,
  "ongoing": false,
  "removed": false,
  "progress_indeterminate": false,
  "progress_max": 0,
  "progress": 0,
  "dnd": 1,
  "package": "de.dlyt.yanndroid.notifer",
  "label": "Notifer",
  "title": "Test Notification",
  "text": "This is a test notification"
}
```

dnd values
from [NotificationManager](https://developer.android.com/reference/android/app/NotificationManager#getCurrentInterruptionFilter()).

### Screenshots

<img loading="lazy" src="readme-res/screenshot_1.jpg" height="350"/> <img loading="lazy" src="readme-res/screenshot_2.jpg" height="350"/> <img loading="lazy" src="readme-res/screenshot_3.jpg" height="350"/> <img loading="lazy" src="readme-res/screenshot_4.jpg" height="350"/> <img loading="lazy" src="readme-res/screenshot_5.jpg" height="350"/> <img loading="lazy" src="readme-res/screenshot_6.jpg" height="350"/> <img loading="lazy" src="readme-res/screenshot_7.jpg" height="350"/>