![Repository Header](./public/banner.png)

# Flaggi 🚩

**A Multiplayer Strategy Game**

Flaggi is a multiplayer strategy game developed by [Samuel](https://github.com/Snapshot20) and [Matěj](https://github.com/kireiiiiiiii) as a submission for the **RHS Videogame Development Club Tournament**. It is built with **Java 8** and uses **Gradle** for dependency management and builds.

---

## 📖 Game Overview

Flaggi is a turn-based multiplayer game where two players take turns placing flags on a grid. The goal is to be the **first player to collect 3 flags** and win the match!

---

## 🚀 Installation

Download the latest release of the **server** (`Server.jar`) and **client app** from the [Releases page](https://github.com/kireiiiiiiii/Flaggi/releases/latest).

You can choose from the following options for the client:

-   `.dmg` for macOS
-   `.exe` for Windows
-   `.jar` (universal) for any platform with Java installed

> **Note:** This is a multiplayer game that requires a server to run.

---

### 🖥️ Running the Server

You can run the server using one of the following methods:

#### **1. Using Java**

```bash
java -jar Server.jar
```

Once the server starts, it logs the **IP address** it's running on. Use this IP to connect clients to the server.

#### **2. Using Docker**

First, ensure Docker is installed on your system. Then, clone this repository and execute the following command in the project root directory:

```bash
./scripts/run.sh docker
```

---

### 🎮 Running the Client

You can run the universal `.jar` client on any platform using:

```bash
java -jar Flaggi.jar
```

Alternatively, use the platform-specific executable (`.exe` or `.dmg`) for Windows or macOS.

---

## 🛠️ Scripts and Packaging

### **run.sh Script**

The `run.sh` script provides multiple ways to execute the project.

-   Run the following command for available options:

```bash
./run.sh -h
```

or

```bash
./run.sh --help
```

### **package.sh Script**

To package the project for specific operating systems, use the `package.sh` script:

```bash
./scripts/package.sh
```

> **Note:**

-   The script builds platform-specific executables (.exe for Windows, .dmg for macOS).
-   To generate a universal `.jar`, run:

```bash
gradle shadowjar
```

-   Build files will be available in:
    -   **Client:** `client/app/build/libs|win|mac`
    -   **Server:** `server/app/build/libs`

---

## 📚 Resources

-   **Font used in banners:** [Ultra](https://fonts.google.com/specimen/Ultra)
-   **Prompt and specifications:** [Game Rules (PDF)](./public/TTT-game-rules.pdf)
-   **Project mindmap:** [Project Map (XMind)](./public/TTT.xmind)

---

## 💬 Contact

Have questions or suggestions? Feel free to:

-   **Create issues** or **submit pull requests** on the [GitHub repository](https://github.com/kireiiiiiiii/Flaggi).
-   Reach out to [@\_kireiiiiiiii](https://www.instagram.com/_kireiiiiiiii) on Instagram.
