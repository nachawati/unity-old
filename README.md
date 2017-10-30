<p align="center">&#1576;&#1587;&#1605; &#1575;&#1604;&#1604;&#1607; &#1575;&#1604;&#1585;&#1581;&#1605;&#1606; &#1575;&#1604;&#1585;&#1581;&#1610;&#1605;</p>
<p align="center">In the name of Allah, the Most Compassionate, the Most Merciful</p>

# Unity DGMS

[![NPM version](http://img.shields.io/npm/v/dgms.svg?style=flat)](http://badge.fury.io/js/dgms)

Unity is a research prototype of a decision guidance management system (DGMS) for rapid decision guidance application development.

## About Unity DGMS
Enterprises across all industries depend on decision guidance to facilitate decision-making across all lines of business. Despite significant technological advances, current development paradigms for decision guidance systems are ad-hoc and lead to systems that exhibit an overly tight coupling of analytical models, algorithms and underlying tools. This inhibits both the reusability and the interoperability of such systems. To address these limitations, a paradigm shift for decision guidance systems development is needed.

Unity is a research prototype of a DGMS, which encapsulates a paradigm shift to reusable analytical model repository-centered development of decision guidance systems.
 
## Installation

Unity DGMS requires [Node.js](https://nodejs.org).
With Node.js installed, you can install Unity DGMS with the following command:

```
npm install -g dgms
```

## Commands

### Login

Most commands provided by Unity DGMS are only available to authenticated users. The login command is used to authenticate your local machine with the Unity DGMS server:

```
dgms login <username>
```

Upon successful authentication, an session token is cached on the local machine and used for subsequent calls to Unity DGMS.

### Logout

```
dgms logout
```

### Identify current user

```
dgms whoami
```

### Initialize new repository
```
dgms init [path]
```

### Show available projects
```
dgms projects
```

### Submit task to Unity DGMS
```
dgms submit <path or expression>
```

### Show task executions
```
dgms tasks
```

### Generate new SSH key
```
dgms key [-o,--overwrite] [-s,--size <length>]
```

### Synchronize local repository
```
dgms sync [path]
```
