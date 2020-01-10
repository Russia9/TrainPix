# TrainPix [![Build Status](https://travis-ci.com/Russia9/TrainPix.svg?branch=master)](https://travis-ci.com/Russia9/TrainPix) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/b92b741a75f641c39ab8c9e1aa9374bb)](https://www.codacy.com/manual/Russia9/TrainPix?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Russia9/TrainPix&amp;utm_campaign=Badge_Grade) [![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0) [![Discord](https://img.shields.io/discord/647795109013749791?label=Discord)](https://discord.gg/D3WTxNw)
Discord bot for [TrainPix](https://trainpix.org/)

## Requirements
- `Docker`

## Installation
To run TrainPix in production, it is recommended to use Docker.
Just pull TrainPix image from Docker Hub and run it with command:
```shell script
 $ docker run --env TRAINPIX_TOKEN={{Bot token}} russia9/trainpix:latest
```

## Usage
| Name | Summary | Parameters | Example | Aliases |
| - | - | - | - | - |
| /help | Display help menu | None | `/help` |  `/help` |
| /list | Display list of trains | Part of train number | `/l ЭП2Д-0` |  `/list`, `/l` |
| /photo | Display photo of train | Train number | `/p ЭР2-1338` |  `/photo`, `/p` |

## Used libraries
- [`log4j2`](https://github.com/apache/logging-log4j2) - Logging
- [`JSoup`](https://jsoup.org/) - Parsing
- [`javacord`](https://github.com/Javacord/Javacord) - Discord API implementation
 
