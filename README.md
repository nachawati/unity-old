<p align="center"><b>&#1576;&#1587;&#1605; &#1575;&#1604;&#1604;&#1607; &#1575;&#1604;&#1585;&#1581;&#1605;&#1606; &#1575;&#1604;&#1585;&#1581;&#1610;&#1605;</b></p>
<p align="center">In the name of Allah, the Most Compassionate, the Most Merciful</p>

# Unity

[![Build Status](https://travis-ci.com/nachawati/unity.svg?token=q1txUcHApehcMiQJcA2S&branch=master)](https://travis-ci.com/nachawati/unity)

Unity is an integrated business analytics platform that aims to simplify the development of intelligent decision-making systems.

## Installation

Unity requires [Node.js](https://nodejs.org).
With Node.js installed, you can install Unity with the following command:

```
npm install -g unity
```

## Unity CLI Commands

### Login

Most commands provided by the Unity CLI are only available to authenticated users. The login command is used to authenticate your local machine with the server:

```
unity login <username>
```

Upon successful authentication, an session token is cached on the local machine and used for subsequent calls to the Unity CLI.

### Logout

```
unity logout
```

### Identify current user

```
unity whoami
```

### Initialize new repository
```
unity init [path]
```

### Show available projects
```
unity projects
```

### Submit task to Unity
```
unity submit <path or expression>
```

### Show task executions
```
unity tasks
```

### Generate new SSH key
```
unity key [-o,--overwrite] [-s,--size <length>]
```

### Synchronize local repository
```
unity sync [path]
```
