# Carrom Royale — 2D LAN Carrom

A portrait, top-down **2D** carrom board inspired by the supplied reference screenshot. In LAN play, each player sees their own striker at the bottom: the guest's board view is rotated 180° so both players have opposite ends. Built as a single-file HTML/CSS/JavaScript app for Android WebView/APK builders.

## Run
Open `index.html` in a modern browser. No build tools or external libraries are required.

## Two-phone LAN
1. Connect both phones to the same Wi-Fi network.
2. On Phone 1 tap **Create room** and copy the OFFER code.
3. Send the offer to Phone 2 (nearby share, chat, QR generator, etc.).
4. On Phone 2 tap **Join room**, paste the offer and tap **Use code**.
5. Send the generated ANSWER back to Phone 1.
6. On Phone 1 paste the answer and tap **Use code**. Wait for CONNECTED.

WebRTC uses host ICE candidates only (no hosted signaling server). Some Android WebView/APK wrappers restrict WebRTC or local-network candidate discovery. Test on the target builder and devices. This is a no-internet-after-connection LAN prototype, not Bluetooth transport.

## Controls
Drag backwards from the striker to aim, release to shoot, and use the power slider. Restart begins a new rack.

## Opposite-side player view\n- Host (Player 1) plays from the bottom edge.\n- Guest (Player 2) sees the same board rotated 180°, so their striker is also at the bottom of their phone.\n- Shot messages are sent over the DataChannel; the shooting phone sends a settled-state snapshot to reduce physics drift.\n\n## Current prototype scope
- 2D canvas board, wood-grain styling, four pockets, decorative markings
- Black/white coins, red queen, striker, aiming line and power control
- Basic friction, wall bounce, coin collision and pocket scoring
- WebRTC DataChannel offer/answer exchange for two phones on the same Wi-Fi

## Not yet production-complete
Official tournament rules (queen cover, due, fouls, break and match-end validation), host-authoritative simulation, complete synchronization/reconnection, audio, and automated device testing remain future work. The initial physics are a lightweight arcade approximation; tune and test before release.

## Android WebView
Enable JavaScript and DOM storage. Use a WebView/APK builder version that supports WebRTC. Keep the app in portrait orientation. For a fully offline packaged app, include this file locally in the APK rather than loading it from a remote website.
