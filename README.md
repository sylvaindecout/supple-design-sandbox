# Supple Design sandbox

[![Tests](https://github.com/sylvaindecout/supple-design-sandbox/actions/workflows/gradle.yml/badge.svg?branch=main)](https://github.com/sylvaindecout/supple-design-sandbox/actions/workflows/gradle.yml) [![Gitmoji](https://img.shields.io/badge/gitmoji-%20%F0%9F%98%9C%20%F0%9F%98%8D-FFDD67.svg)](https://gitmoji.dev)

## Usage

*Note: This project depends on a personal GitHub Package Registry. In order to build locally, it is necessary to set
USERNAME and TOKEN environment variables. Instructions for the generation of a token are available in
[Creating a personal access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token).*

Run tests: `./gradlew test`

## Context

![Context](./doc/images/context.svg)

### Interfaces

![Interfaces](./doc/images/interfaces.svg)

### Business logic

![Sequence](./doc/diagrams/generated/sequence.svg)

### Considered evolutions

 * Do not check availability of ingredients
 * Consider all ingredients as available in case Stock service is down
 * Save invoices in DB
 * Make preparation step blocking
