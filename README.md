<div align="center">
  
<img width="96" height="96" alt="03071a6e14f91b869e37817354b29091ed2bfbcd_96" src="https://github.com/user-attachments/assets/1109c2f1-33c2-4c49-bf30-0213e1466d12" />

# TagsKeepers
Plugin de tags optimizado y eficiente para servidores 1.20 en adelante

[![Version](https://img.shields.io/badge/version-1.4-green)]()
[![Idioma](https://img.shields.io/badge/language-Java%2021-orange)]()
[![Paper](https://img.shields.io/badge/Paper-1.20.4-yellow)]()

</div>

---

## Contenido

- [Caracteristicas](#caracteristicas)
- [Dependencias](#dependencias)
- [Instalacion](#instalacion)
- [Comandos y Permisos](#comandos-y-permisos)
- [Configuracion de Tags](#configuracion-de-tags)
- [Menu GUI](#menu-gui)
- [Sistema de Economia](#sistema-de-economia)
- [PlaceholderAPI](#placeholderapi)
- [Colores HEX](#colores-hex)
- [Compilacion](#compilacion)

---

## Caracteristicas

- Tags con formato de color usando codigos `&` y HEX `&#RRGGBB`
- Menu visual paginado con slots configurables por tag
- Compra de tags mediante Vault con cualquier proveedor de economia (EssentialsX, CMI, etc.)
- Placeholder `%tagskeeper_tag%` para mostrar el tag en chat, tablist, o donde se use PlaceholderAPI
- La seleccion y compras se guardan en `data.yml` por UUID (persiste tras cambios de nombre)
- Permiso especifico por tag (`tagskeeper.<id>`) y compatibilidad legacy (`alonsotags.tag.<id>`)
- Sonidos configurables por accion (abrir, seleccionar, denegar, ya-seleccionado)
- Lore de tags con placeholders: `%preview%`, `%status%`, `%selected%`, `%price%`
- PlaceholderAPI y Vault son soft-depend: el plugin funciona sin ellos, las funciones correspondientes se desactivan
- Argumentos de consola: ningun comando requiere argumentos

---

## Dependencias

| Dependencia | Tipo | Necesaria para |
|---|---|---|
| Paper 1.20.x | Servidor | Ejecucion |
| PlaceholderAPI | Plugin (soft-depend) | Placeholder `%tagskeeper_tag%` |
| Vault + Economy Provider | Plugin (soft-depend) | Compra de tags con precio |

Si Vault no esta instalado, `EconomyHook.setup()` falla silenciosamente y `EconomyHook.isEnabled()` retorna `false`. Los tags con `price` simplemente no muestran precio ni permiten compra.

---

## Instalacion

1. Descarga el JAR desde [Releases](https://github.com/DevAdvvy/TagsKeepers/releases)
2. Colocalo en `plugins/`
3. Reinicia el servidor
4. Configura los tags en `plugins/TagsKeepers/tags.yml`
5. Ejecuta `/reloadtags` para aplicar cambios sin reiniciar

En primer arranque el plugin genera los archivos por defecto:
- `config.yml` (solo indica `storage.type: YAML`)
- `messages.yml`
- `menu.yml`
- `tags.yml` (viene con 9 tags de ejemplo)
- `data.yml` (vacio, se llena automaticamente)

---

## Comandos y Permisos

### Comandos

| Comando | Aliases | Descripcion | Uso en consola |
|---|---|---|---|
| `/tags` | `/tag` | Abre el menu de tags (pagina 1) | No, solo retorna true sin accion |
| `/reloadtags` | -- | Recarga configuracion, mensajes, menu y tags | Si |

`/tags` no verifica permisos. Cualquier jugador puede abrir el menu. Los tags individuales se controlan por permiso interno.

### Permisos

| Nodo | Default | Descripcion |
|---|---|---|
| `tagskeeper.perms.reload` | `op` | Acceso a `/reloadtags` |
| `tagskeeper.bypass.price` | `op` | Obtiene tags de pago sin descontar dinero |
| `tagskeeper.<id>` | -- | Acceso al tag con ese ID (ej: `tagskeeper.vip`) |
| `alonsotags.tag.<id>` | -- | Permiso legacy, se verifica como alternativa al anterior |

---

## Configuracion de Tags

`tags.yml` contiene la definicion de los tags bajo la clave `tags:`.

### Formato

```yaml
tags:
  <id>:
    tag: "<nombre que muestra en el chat/scoreboard/tablist>"
    menu-display: "<nombre en el menu>"
    preview: "<vista previa con {player}>"
    permission: "tagskeepers.example <permiso requerido>"
    material: "<Material>"
    page: <numero de pagina>
    slot: <slot en el inventario>
    price: <precio opcional, sin comillas: 10000>
    lore:
      - "<linea de lore>"
```

### Campos

| Campo | Requerido | Default | Tipo |
|---|---|---|---|
| `tag` | Si | -- | String (colores) |
| `menu-display` | Si | -- | String (colores) |
| `preview` | Si | -- | String (colores, debe contener `{player}`) |
| `permission` | Si | -- | String (nodo de permiso) |
| `material` | Si | -- | String (Bukkit Material, ej: `NAME_TAG`, `DIAMOND`) |
| `slot` | Si | -- | Integer (0-53 segun el tamanio del menu) |
| `page` | No | `1` | Integer |
| `lore` | Si | -- | Lista de strings |
| `price` | No | `-1` (sin precio) | Double |

### Placeholders en el lore

| Placeholder | Se reemplaza por |
|---|---|
| `%preview%` | El campo `preview` con `{player}` remplazado por el nombre del jugador |
| `%status%` | El mensaje configurado en `messages.yml` segun `tag-status.unlocked` o `tag-status.locked` |
| `%selected%` | El mensaje configurado en `messages.yml` segun `tag-selection.selected` o `tag-selection.not-selected` |
| `%price%` | El precio formateado por Vault si el tag tiene `price` y Vault activo; string vacio en caso contrario |

### Ejemplo completo

```yaml
tags:
  vip:
    tag: "&8&#FFD700&lVIP&8"
    menu-display: "&fTag &8&#FFD700&lVIP&8"
    preview: "&8&#FFD700&lVIP &f{player}"
    permission: "tagskeeper.vip"
    material: NAME_TAG
    page: 1
    slot: 10
    price: 10000
    lore:
      - ""
      - "&7Vista Previa:"
      - "&f%preview%"
      - ""
      - "&7Estado: %status%"
      - "&7Precio: &e%price%"
      - ""
      - "%selected%"
```

### Tags por defecto incluidos

| ID | Pagina | Slot | Precio |
|---|---|---|---|
| campeon | 1 | 10 | -- |
| mvp | 1 | 11 | -- |
| elite | 1 | 12 | -- |
| random | 1 | 13 | -- |
| veterano | 1 | 14 | -- |
| og | 1 | 15 | -- |
| koth | 1 | 16 | -- |
| emperor | 2 | 13 | -- |
| king | 3 | 13 | -- |

Ningun tag por defecto incluye `price`.

---

## Menu GUI

Configuracion en `menu.yml`.

### Paginas

Cada pagina se define como `menus.page-<numero>`. El plugin trae 3 paginas preconfiguradas.

```yaml
menus:
  page-1:
    title: "<titulo del inventario>"
    size: 36
    next-page-slot: 32
    previous-page-slot: <opcional>
    remove-tag-slot: 31
```

| Opcion | Descripcion |
|---|---|
| `title` | Titulo del inventario (soporta colores HEX) |
| `size` | Tamanio del inventario, multiplo de 9 (tipico: 27, 36, 45, 54) |
| `next-page-slot` | Slot donde aparece la flecha de siguiente pagina |
| `previous-page-slot` | Slot donde aparece la flecha de pagina anterior |
| `remove-tag-slot` | Slot del item para remover el tag actual |

Si una pagina no tiene `next-page-slot` o `previous-page-slot`, esa flecha no se muestra.

### Filler

```yaml
menus:
  fill-empty-slots: true
  filler:
    material: BLACK_STAINED_GLASS_PANE
    name: " "
```

Si `fill-empty-slots` es `true`, se llenan todos los slots (0 a size-1) con el material configurado antes de colocar los items de tags/navegacion. Esto evita que los jugadores muevan items al inventario. Los items de tags sobrescriben el filler en sus slots especificos.

### Sonidos

```yaml
menus:
  sounds:
    open: BLOCK_CHEST_OPEN
    select: ENTITY_PLAYER_LEVELUP
    denied: ENTITY_VILLAGER_NO
    already-selected: BLOCK_NOTE_BLOCK_BASS
```

Los sonidos se reproducen con volumen 1.0 y pitch 1.0. El nombre debe corresponder a un valor de `org.bukkit.Sound`. Si el nombre es invalido, se registra una advertencia en la consola y se omite el sonido.

### Remove-tag item

```yaml
menus:
  remove-tag:
    enabled: true
    active:
      material: PLAYER_HEAD
      basehead: "<base64>"
      name: "&fMenu &8» &8&#FFD700&ltags&8 &f"
      lore:
        - " &fTag Actual:"
        - "   %current_tag%"
        - " &8- &fClick para remover"
    inactive:
      material: GRAY_DYE
      name: "&fMenu &8» &8&#FFD700&ltags&8 &f"
      lore:
        - " &7Actualmente no tienes"
        - " &7ningun tag activo."
```

El item `active` se muestra cuando el jugador tiene un tag equipado. El item `inactive` cuando no. El placeholder `%current_tag%` en el lore se reemplaza por el tag actual formateado, o `"&7None"` si el tag ID no se encuentra en el registro.

### Navegacion

Las flechas de navegacion se crean con material `ARROW`:
- Siguiente pagina: nombre `"&aSiguiente Pagina"`
- Pagina anterior: nombre `"&cPagina Anterior"`

(Textos en espanol, hardcodeados en MenuManager.java.)

---

## Sistema de Economia

### Requisitos

- Vault instalado en el servidor
- Un proveedor de economia registrado en Vault (EssentialsX, CMI, CosmicPvP, etc.)

### Funcionamiento interno

`EconomyHook` usa reflection para acceder a `net.milkbowl.vault.economy.Economy`, por lo que no requiere VaultAPI en compilacion. En runtime:

1. `EconomyHook.setup()` se llama desde `Main.onEnable()`
2. Verifica que el plugin `Vault` exista en el servidor
3. Obtiene el proveedor de economia via `Bukkit.getServicesManager().getRegistration()`
4. Almacena los metodos `has()`, `withdrawPlayer()`, `format()` via reflection
5. Si falla en cualquier paso, `isEnabled()` retorna `false`

### Flujo de compra

Cuando un jugador hace click en un tag para el que no tiene permiso:

1. Si el tag no tiene `price` (> 0) o la economia no esta activa: mensaje de `no-permission`
2. Si el tag tiene `price` y la economia esta activa:
   - Si el jugador tiene `tagskeeper.bypass.price`: se marca como comprado sin cobrar, se selecciona el tag
   - Si el jugador no tiene saldo suficiente: mensaje de `tag-insufficient-funds`
   - Si tiene saldo: se ejecuta `withdraw()`, se marca como comprado en `data.yml`, se selecciona el tag

La compra se guarda en `data.yml` como:

```yaml
purchased:
  <uuid>:
    <tag-id>: true
```

El tag comprado queda vinculado al UUID permanentemente. No expira.

### Mensajes relacionados

| Ruta | Placeholders |
|---|---|
| `tag-purchased` | `%prefix%`, `%price%` |
| `tag-insufficient-funds` | `%prefix%`, `%price%` |
| `tag-bypass-purchase` | `%prefix%`, `%price%` |

---

## PlaceholderAPI

Si PlaceholderAPI esta presente en el servidor, el plugin registra la expansion `tagskeeper` con el placeholder:

| Placeholder | Retorno |
|---|---|
| `%tagskeeper_tag%` | El valor del campo `tag` del tag seleccionado (con colores), o string vacio si no tiene tag o el ID no existe |

La expansion se registra en `Main.onEnable()` si `PlaceholderAPI` se detecta como plugin cargado.

### Uso tipico

En plugins compatibles con PlaceholderAPI (TAB, EssentialsXChat, CMI, DeluxeChat, etc.):

```
Formato en config: %tagskeeper_tag%
Ejemplo en chat:   &8&#FFD700&lVIP &7DevAdvvy: Hola
```

---

## Colores HEX

El plugin usa `ColorUtil.java` que convierte el formato `&#RRGGBB` al equivalente de BungeeCord `ChatColor`.

### Formato

```
&#RRGGBB
```

Donde RR, GG, BB son valores hexadecimales (00-FF).

### Ejemplos

```
&#FF0000  Rojo
&#00FF00  Verde
&#0000FF  Azul
&#FFD700  Dorado
&#E59500  Naranja
```

### Combinacion con codigos de formato

```
&#FFD700&l   Dorado + negrita
&#FF6A00&l&o Naranja + negrita + italica
&8&#FFD700   Gris oscuro + Dorado (sin formato)
```

Los codigos `&` (0-9, a-f, k-o, r) de Minecraft estandar funcionan junto con los HEX.

### Donde se puede usar

- `tag` en tags.yml
- `menu-display` en tags.yml
- `preview` en tags.yml
- `lore` en tags.yml
- `title` en menu.yml
- Todos los mensajes en messages.yml
- `name` del filler y remove-tag en menu.yml

---

## Mensajes

`messages.yml` contiene todos los textos que el plugin envia a los jugadores.

### Placeholders globales

| Placeholder | Descripcion |
|---|---|
| `%prefix%` | Se reemplaza por el valor de `prefix` en messages.yml |
| `%tag%` | (Solo en `tag-selected`) El tag seleccionado, con colores |
| `%price%` | (Solo en mensajes de compra) Precio formateado por Vault |

### Lista completa de mensajes

```yaml
prefix: "<prefijo del plugin>"
no-permission: "%prefix% <sin permiso>"
tag-selected: "%prefix% <tag seleccionado: %tag%>"
tag-already-selected: "%prefix% <ya seleccionado>"
tag-removed: "%prefix% <tag removido>"
no-tag-equipped: "%prefix% <sin tag equipado>"
reload-success: "%prefix% <recargado>"
tag-purchased: "%prefix% <comprado por %price%>"
tag-insufficient-funds: "%prefix% <dinero insuficiente: %price%>"
tag-bypass-purchase: "%prefix% <obtenido gratuitamente>"
```

---

## Ciclo de datos

### Al iniciar el servidor

1. `Main.onEnable()` guarda los archivos YAML por defecto si no existen
2. `ConfigManager` carga `messages.yml` y `menu.yml` a memoria
3. `YAMLStorage` carga `data.yml` (o lo crea si no existe)
4. `TagManager` carga `tags.yml` a memoria en un `HashMap<String, Tag>`
5. `PlayerDataManager` inicializa un cache vacio `Map<UUID, PlayerTagData>`
6. `EconomyHook.setup()` intenta conectar con Vault

### Cuando un jugador se conecta

`JoinListener.onJoin()`:
1. Lee de `data.yml` el tag seleccionado del jugador (`config.getString(uuid)`)
2. Lo guarda en el cache de `PlayerDataManager`

### Cuando un jugador selecciona un tag

`InventoryListener.onClick()`:
1. Actualiza `PlayerTagData.selectedTag` en memoria
2. Guarda en `data.yml` via `YAMLStorage.saveTag(uuid, tagId)`

### Cuando se ejecuta `/reloadtags`

`ReloadCommand.onCommand()`:
1. `reloadConfig()` (recarga config.yml)
2. `ConfigManager.reload()` (recarga messages.yml y menu.yml)
3. `TagManager.loadTags()` (limpia y recarga tags.yml)
4. No afecta a `data.yml` ni al cache de `PlayerDataManager`

---

## Compilacion

```bash
git clone https://github.com/DevAdvvy/TagsKeepers.git
cd TagsKeepers
mvn clean package
```

El JAR se genera en `target/TagsKeepers-1.3.jar`.

### Dependencias de compilacion

Las dependencias (Paper API, PlaceholderAPI, authlib) se resuelven desde los repositorios Maven configurados en `pom.xml`. No se incluyen en el JAR final (scope `provided`).

---

## Notas tecnicas

- El plugin no modifica el chat por si mismo. El tag se expone via PlaceholderAPI para que otros plugins lo muestren donde corresponda.
- Los datos de jugador se almacenan por UUID, no por nombre. Si un jugador cambia de nombre, su tag y compras se conservan.
- `data.yml` se escribe en disco en cada operacion de guardado (`save()`). No hay buffer ni escritura diferida.
- El TagManager almacena los tags en un `HashMap<String, Tag>` donde la clave es el ID en minusculas.
- `EconomyHook` usa `player.getLocation()` como parametro de `Player` para las llamadas a Vault. Vault acepta `OfflinePlayer`, y `Player` extiende `OfflinePlayer`.
- La validacion de slots en `InventoryListener.onClick()` usa `stream().anyMatch()` para verificar que el slot clickeado corresponde a un tag en la pagina actual antes de iterar la busqueda.
- Los sonidos se manejan con try-catch por si el nombre configurado no corresponde a un Sound valido.
- El Base64 de las cabezas se valida con regex `[a-zA-Z0-9_\-]+` sobre el texture ID extraido para prevenir SSRF hacia dominios que no sean `textures.minecraft.net`.
