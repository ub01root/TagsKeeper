# TagsKeeper

> Advanced and customizable Minecraft tag system with modern support for Hex Colors, PlaceholderAPI, and permissions.

![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1+-green?style=for-the-badge)
![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge)
![Platform](https://img.shields.io/badge/Platform-Paper%20%7C%20Spigot%20%7C%20Purpur-blue?style=for-the-badge)

---

## ✨ Features

- 🎨 Full Hex Color support
- ⚡ Compatible from **1.20.1** up to the latest Minecraft versions
- 🔗 PlaceholderAPI support
- 🔐 Permission-based tag system
- 🛠️ Fully customizable configuration
- 📋 Easy-to-use placeholders
- 🚀 Lightweight and optimized

---

# 📦 Compatibility

| Software | Versions |
|---|---|
| Minecraft | 1.20.1 → Latest |
| Server Software | Paper, Spigot, Purpur |
| Java | Java 17+ |

---

# 🔑 Permissions

Each tag has its own permission node:

```yaml
tagskeeper.{idtag}
```

### Example

```yaml
tagskeeper.owner
tagskeeper.vip
tagskeeper.legend
```

---

# 🔗 PlaceholderAPI Support

TagsKeeper supports custom placeholders:
```yaml
%tagskeeper_tag%
```


Perfect for:

- Scoreboards
- TAB
- Chat formats
- Nametags
- ActionBars

---

# 🎨 Hex Color Support

Use modern RGB hex colors directly inside your tags.

### Example

```yaml
display: "&#ff4d4dOwner"
```

---

# ⚙️ Installation

1. Download the plugin `.jar`
2. Move it to your server's `plugins/` folder
3. Restart the server
4. Configure your tags
5. Enjoy ✨

---

# 📁 Configuration

Plugin folder:

```yaml
/plugins/TagsKeeper/
```

Included files:

```yaml
config.yml
tags.yml
messages.yml
menu.yml
```

---

# 🛠️ Recommended Plugins

- PlaceholderAPI
- LuckPerms

---

# 💡 Example Tag

```yaml
owner:
  display: "&#ff0000OWNER"
  permission: "tagskeeper.owner"
```

---

# 📜 License

TagsKeeper © 2026  
All rights reserved.
