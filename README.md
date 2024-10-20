# Youtube-Downloader

## Overview

This project is a comprehensive YouTube video and playlist downloader and converter. It allows users to download and convert YouTube content into various formats, providing a flexible tool for managing and enjoying online videos offline.

## Table of Contents

- [Getting Started](#getting-started)
- [Features](#features)
- [Usage](#usage)
  - [Command-Line Interface](#command-line-interface)
  - [Downloading Videos](#downloading-videos)
  - [Downloading Playlists](#downloading-playlists)
  - [Converting Formats](#converting-formats)
- [Configuration](#configuration)
- [Contributing](#contributing)
- [License](#license)

## Getting Started

To get started with this project, clone the repository and ensure you have the necessary prerequisites installed.

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Maven

### Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/luanmxz/Youtube-Downloader.git
    ```
2. Navigate to the project directory:
    ```bash
    cd Youtube-Downloader
    ```
3. Install dependencies using Maven:
    ```bash
    mvn install
    ```

## Features

- **Download YouTube videos**: Save individual videos locally.
- **Download entire YouTube playlists**: Download all videos from a playlist.
- **Convert downloaded videos**: Convert videos to various formats like MP3, MP4.
- **Simple command-line interface**: Easy-to-use commands for downloading and converting.

## Usage

### Command-Line Interface

The application can be run from the command line using the following command:
```bash
java -jar target/Youtube-Downloader-1.0.jar
```

### Downloading Videos

To download a single video, use:
```bash
java -jar target/Youtube-Downloader-1.0.jar --url <YouTube-Video-URL>
```

### Downloading Playlists

To download a playlist, use:
```bash
java -jar target/Youtube-Downloader-1.0.jar --playlist <YouTube-Playlist-URL>
```

### Converting Formats

To convert a downloaded video to a specific format, such as MP3:
```bash
java -jar target/Youtube-Downloader-1.0.jar --convert <Downloaded-Video-Path> --format mp3
```

## Configuration

The application can be configured using a properties file to set various options such as download paths, default formats, and other preferences.

### Example Configuration File

```properties
# Configuration file for Youtube-Downloader

# Path where downloaded videos will be saved
download.path=/path/to/download/folder

# Default format for video conversion
default.format=mp4

# Enable verbose logging
logging.verbose=true
```

To use a custom configuration file, specify the file path when running the application:
```bash
java -jar target/Youtube-Downloader-1.0.jar --config /path/to/config.properties
```

## Contributing

Contributions are welcome! Please fork this repository and submit pull requests for any improvements or new features you would like to add.

### Steps to Contribute

1. Fork the repository
2. Create a new branch (`git checkout -b feature-branch`)
3. Commit your changes (`git commit -m 'Add some feature'`)
4. Push to the branch (`git push origin feature-branch`)
5. Open a pull request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
