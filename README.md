# Wrapper for Argo workflows CLI

Interactive shell to wrap Argo CLI commands.

## Overview

### Installation

Argo wrapper can be installed from [GitHub releases](https://github.com/sbs-software/argo-wrapper/releases).

### Usage

#### non-interactive mode

```shell
$ argow list
NAME               STATUS      AGE   DURATION   PRIORITY   MESSAGE
test-batch-4b5n7   Succeeded   18h   21s        0

$ 
```

#### interactive mode

```shell
$ argow
[cluster: -] [namespace: -] [workflow: -]
argow:> init

[cluster: test] [namespace: test-ns] [workflow: -]
argow:> list
NAME               STATUS      AGE   DURATION   PRIORITY   MESSAGE
test-batch-4b5n7   Succeeded   18h   21s        0

[cluster: test] [namespace: test-ns] [workflow: -]
argow:> get test-batch-4b5n7
...

[cluster: test] [namespace: test-ns] [workflow: test-batch-4b5n7]
argow:> exit

$ 
```

### Argo Wrapper Commands

- [**get**: Get an Argo workflow](#get)
- [**list**: List Argo workflows](#list)
- [**retry**: Retry an Argo workflow](#retry)

### Context commands

Context commands are only available in interactive mode.

- [**context clear**](#context-clear), [**init**](#init): Reset or initialize all parameters of Argo wrapper context
- [**context set**](#context-set): Set parameter in Argo wrapper context
- [**context kube list**](#context-kube-list): List kubernetes contexts
- [**context kube set**](#context-kube-set): Set kubernetes context

## Commands

### get

```
NAME
       get - Get an Argo workflow

SYNOPSIS
       get [--id string] [--namespace string] [--help] 

OPTIONS
       --id string
       Unique ID of the workflow to retry.
       In interactive mode, defaults to the workflow of Argo wrapper context
       [Optional]

       --namespace or -n string
       Namespace of the workflow to retry.
       Defaults to the namespace of current Kube context
       [Optional]

       --help or -h 
       help for get
       [Optional]
```

### list

```
NAME
       list - List Argo workflows

SYNOPSIS
       list [--namespace string] [--help] 

OPTIONS
       --namespace or -n string
       Namespace of the workflow to retry.
       Defaults to the namespace of current Kube context
       [Optional]

       --help or -h 
       help for list
       [Optional]
```

### retry

```
NAME
       retry - Retry an Argo workflow

SYNOPSIS
       retry [--id string] [--namespace string] [--target string] [--skip-dependencies boolean] [--parameter param1=value1 param2=value2] [--watch boolean] [--help] 

OPTIONS
       --id string
       Unique ID of the workflow to retry.
       In interactive mode, defaults to the workflow of Argo wrapper context
       [Optional]

       --namespace or -n string
       Namespace of the workflow to retry.
       Defaults to the namespace of current Kube context
       [Optional]

       --target or -t string
       Name (displayName) of the task to retry in a successful DAG workflow
       [Optional]

       --skip-dependencies or --skip or -s boolean
       Skip dependent tasks when retrying a task in a successful DAG workflow
       [Optional, default = false]

       --parameter or -p param1=value1 param2=value2
       Input parameters added to the retried workflow
       [Optional]

       --watch or -w boolean
       Watch the workflow until it completes
       [Optional, default = false]

       --help or -h 
       help for retry
       [Optional]
```

### context clear

```
NAME
       context clear - Reset or initialize all parameters of Argo wrapper context

SYNOPSIS
       context clear [--help]

OPTIONS
       --help or -h 
       help for context clear
       [Optional]


ALSO KNOWN AS
       init
```

### context set

```
NAME
       context set - Set parameter in Argo wrapper context

SYNOPSIS
       context set --parameter namespace|workflow --value string [--help]

OPTIONS
       --parameter namespace|workflow
       Name of the parameter to store in context
       [Mandatory]

       --value string
       Value of the parameter to store in context
       [Mandatory]

       --help or -h 
       help for context set
       [Optional]
```

### context kube list

```
NAME
       context kube list - List kubernetes contexts

SYNOPSIS
       context kube list [--help]

OPTIONS
       --help or -h 
       help for context kube list
       [Optional]
```

### context kube set

```
NAME
       context kube set - Set kubernetes context

SYNOPSIS
       context kube set --context context [--help]

OPTIONS
       --context context
       Name of the kubernetes context
       [Mandatory]

       --help or -h 
       help for context kube set
       [Optional]
```

### init

Alias of [context clear](#context-clear)

## Technical Details

argow is a Spring shell application compiled as a GraalVM native image.