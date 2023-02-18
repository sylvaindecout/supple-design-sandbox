# Supple Design sandbox

[![Tests](https://github.com/sylvaindecout/supple-design-sandbox/actions/workflows/gradle.yml/badge.svg?branch=main)](https://github.com/sylvaindecout/supple-design-sandbox/actions/workflows/gradle.yml) [![Gitmoji](https://img.shields.io/badge/gitmoji-%20%F0%9F%98%9C%20%F0%9F%98%8D-FFDD67.svg)](https://gitmoji.dev)

## Usage

*Note: This project depends on a personal GitHub Package Registry. In order to build locally, it is necessary to set
USERNAME and TOKEN environment variables. Instructions for the generation of a token are available in
[Creating a personal access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token).*

Run tests: `./gradlew test`

## Context

### User journey

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="doc/images/context_dark.png" />
  <img src="doc/images/context_light.png" alt="Context" title="Context" width="650px" />
</picture>

### Interfaces

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="doc/images/interfaces_dark.svg" />
  <img src="doc/images/interfaces_light.svg" alt="Interfaces" title="Interfaces" />
</picture>

### Business logic

```mermaid
sequenceDiagram
    participant Client
    box rgba(88, 214, 141, 0.3)
        participant Ordering
    end
    participant Menu
    participant Stock
    participant Invoices
    participant Preparation as Drink<br/>Preparation

    Client ->> +Ordering : Order
    Ordering ->> +Menu : Get recipe
    Menu -->> -Ordering : Recipe
    loop for each ingredient in recipe
        Ordering ->> +Stock : Get available quantity
        Stock -->> -Ordering : Available quantity
    end
    alt required quantities are available
        Ordering -->> +Preparation: Queue order
        Ordering ->> Invoices: Save invoice
        Ordering -->> -Client : Invoice
    end
```

### Considered evolutions

 * Do not check availability of ingredients
 * Consider all ingredients as available in case Stock service is down
 * Check customer account for possible discounts
 * Make preparation step blocking
