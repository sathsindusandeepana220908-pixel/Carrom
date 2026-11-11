# Android APK packaging notes

1. Download the repository ZIP from GitHub.
2. Extract it. Keep `index.html`, `styles.css` and `game.js` together in the same folder.
3. In your APK builder, choose the local HTML / WebView app option and select `index.html` as the start page.
4. Enable JavaScript, local file access as needed by that builder, WebRTC and DataChannels, and network access.
5. Set portrait orientation and a recent Android WebView/Chromium runtime.
6. Build a debug APK first. Install it on both phones and test the LAN offer/answer connection.
7. Only create a release APK after the full two-phone test passes.

## Important

- The game does not need a hosted game server or internet once a peer-to-peer LAN connection is established.
- WebRTC support in Android WebView varies by APK builder. If `RTCPeerConnection` is unavailable, that builder cannot run this multiplayer implementation without a more capable native wrapper.
- The manual offer/answer exchange is intentional in this no-signalling-server version.
- Bluetooth is not implemented. This project uses Wi-Fi LAN with WebRTC.
