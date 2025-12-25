# CSMA Network Simulation (CSMA/CD & CSMA/CA)

A multi-threaded Java application that simulates the behavior of Medium Access Control (MAC) protocols. This project models how multiple network stations compete for a shared communication channel using **Carrier Sense Multiple Access with Collision Detection (CSMA/CD)** and **Carrier Sense Multiple Access with Collision Avoidance (CSMA/CA)**.

## 📌 Project Overview
The simulation creates an environment with **3 network stations**, where each station attempts to transmit **2 data packets**. It effectively demonstrates concurrency issues and protocol mechanisms such as:
* **Carrier Sensing:** Stations check the channel status (`IDLE` or `INUSE`) before attempting transmission.
* **Random Back-off:** Stations wait for a random duration before retrying after a busy channel or a collision.
* **Collision Handling:** Different strategies for managing simultaneous transmission attempts.

---

## 🛠️ Technical Implementation

### Core Architecture
* **Multi-threading:** Each station is implemented as an independent `Thread` (via the `Runnable` interface), allowing for realistic concurrent behavior.
* **Shared State:** A global `ChannelStatus` variable (using an interface `FREEINUSE`) tracks whether the medium is currently occupied.
* **Thread Safety:** The simulation uses `AtomicBoolean` to manage the success state of transmissions across threads safely.



### Protocol Logic
| Feature | CSMA/CD Implementation | CSMA/CA Implementation |
| :--- | :--- | :--- |
| **Detection** | Monitors the channel state during the `Send()` process. | Relies on the reception of an Acknowledgment (ACK). |
| **Collision Rule** | If the channel is occupied while trying to transmit, a collision is flagged immediately. | If a random probability check for ACK fails, a collision is assumed. |
| **Retransmission** | Uses a back-off time based on the number of collisions. | Uses a fixed random range for the back-off timer. |

---