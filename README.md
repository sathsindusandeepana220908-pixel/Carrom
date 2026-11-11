# Carrom Royale — 2D Carrom

A portrait, top-down 2D carrom game inspired by the reference screenshot. It is built with plain HTML, CSS and JavaScript Canvas: no frameworks, build step, external fonts, CDN assets or game server.

## Files

- `index.html` — app layout and entry point
- `styles.css` — responsive mobile-first styling
- `game.js` — board rendering, input, physics, casual rules and LAN multiplayer

## Run locally

Open `index.html` in a modern browser. For best results, serve this folder from a local static web server. The game itself has no external asset dependencies.

## Controls

1. Touch the striker or the baseline near the bottom of the board to position it.
2. Drag backwards to aim. Release to shoot.
3. Set shot strength with the power slider.
4. Use **New Game** to reset the rack and **Rules** to see the in-game rules.
5. In local mode, the view rotates on Player 2's turn so the active player always shoots from the bottom.

## Casual rules included

- Player 1 uses white coins; Player 2 uses black coins.
- Basic striker movement, coin-to-coin collisions, friction and cushion rebounds.
- Four corner pockets and scoring by coin colour.
- Pocketing your own colour keeps your turn; otherwise the turn passes.
- Queen cover: after pocketing the queen, pocket one of your own coins on that shot or your next shot.
- Striker pocket is treated as a foul; one point is deducted where possible and the turn passes.
- A player wins after clearing their colour and covering the queen.

This is a playable casual ruleset, not a certified tournament rules implementation. Official federation rules differ on break shots, due/penalty coins, fouls, queen return, end-game scoring and match points. The current physics are an arcade approximation and should be tuned with physical-device playtesting.

## Two-phone LAN multiplayer

This uses WebRTC DataChannel with no STUN/TURN server configured. The host runs the physics simulation and sends board snapshots to the guest, so the guest does not independently simulate the same shot. Player 1 sees the normal board; Player 2 sees the board rotated 180 degrees so their striker appears at the bottom of their own phone.

1. Connect both phones to the same Wi-Fi network. Avoid guest Wi-Fi or access points with client/AP isolation enabled.
2. On Phone 1, tap **Create Room**. Copy the OFFER code and send it to Phone 2 using a messaging app, nearby share, or another method.
3. On Phone 2, tap **Join Room**, paste the OFFER, then tap **Use Code**.
4. Phone 2 displays an ANSWER code. Send that code back to Phone 1.
5. Phone 1 pastes the ANSWER and taps **Use Code**. Wait until both phones show **Connected**.
6. Player 1 and Player 2 take turns. The host is Player 1; the joining phone is Player 2.

The offer/answer exchange is manual signalling. No matchmaking, room list, QR exchange, public server, STUN or TURN relay is included. Local peer discovery and WebRTC support depend on the browser/WebView and Wi-Fi router. Some networks block device-to-device traffic. If the connection does not establish, try a normal private Wi-Fi network with client isolation disabled and test in current Chrome on both phones first.

The host is authoritative for the physics and game state. This is a small casual-game implementation, not a cheat-resistant competitive network protocol. Reconnection recovery, latency compensation, packet-loss recovery, host migration and secure room authentication are not implemented.

## Android APK builder checklist

Package the complete folder while preserving these relative paths:

- `index.html`
- `styles.css`
- `game.js`

Configure the builder/WebView to:
- open the local `index.html` file as the start page
- enable JavaScript
- allow WebRTC / `RTCPeerConnection` and DataChannels
- allow local network access and Android INTERNET permission (used by WebView networking, even for local peer traffic)
- keep the app in portrait orientation
- use a recent Android System WebView / Chromium version
- avoid loading the game from a remote website if you want the app assets bundled offline

Some APK builders do not expose WebRTC in their WebView. If LAN buttons fail or `RTCPeerConnection` is undefined, use a builder that supports modern Chromium WebRTC or build a native Android WebView wrapper with WebRTC support. A normal HTML-only wrapper cannot add Bluetooth transport by itself.

## Test before release

- [ ] Open the app on two different Android phones.
- [ ] Play a complete local two-player match on one phone.
- [ ] Confirm the guest's board is rotated and their striker is at the bottom.
- [ ] Connect both phones over the same Wi-Fi and complete offer/answer exchange.
- [ ] Verify both players can shoot only on their own turn.
- [ ] Pocket white, black, queen and striker coins and compare both screens.
- [ ] Test restart, disconnect, background/foreground and screen rotation.
- [ ] Test on the exact APK builder and Android versions you intend to distribute.

No APK has been built or device-tested by this repository update. Build and test the packaged app on your phones before sharing it.
