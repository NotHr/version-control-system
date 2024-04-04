# Version Control System

---

## Setup

```bash
git clone https://codeberg.org/nothr/version-control-system
```

```bash
cd version-control-system
```

```bash
cd src
```

```bash
javac in/nothr/vcs/core/VCS.java
```

```bash
java in.nothr.vcs.core.VCS init
java in.nothr.vcs.core.VCS add <file>
java in.nothr.vcs.core.VCS commit <message>
java in.nothr.vcs.core.VCS reset <commithash> [--SOFT | --MIXED]
```
