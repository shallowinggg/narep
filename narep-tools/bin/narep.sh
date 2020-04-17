#!/bin/sh

if [ -z "$NAREP_HOME" ] ; then
  ## resolve links - $0 may be a link to maven's home
  PRG="$0"

  # need this for relative symlinks
  while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
      PRG="$link"
    else
      PRG="`dirname "$PRG"`/$link"
    fi
  done

  saveddir=`pwd`

  NAREP_HOME=`dirname "$PRG"`/..

  # make it fully qualified
  NAREP_HOME=`cd "$NAREP_HOME" && pwd`

  cd "$saveddir" || return
fi

export NAREP_HOME

error_exit ()
{
    echo "ERROR: $1 !!"
    exit 1
}

[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=$HOME/jdk/java
[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/usr/java
[ ! -e "$JAVA_HOME/bin/java" ] && error_exit "Please set the JAVA_HOME variable in your environment, We need java(x64)!"

export JAVA_HOME
export JAVA="$JAVA_HOME/bin/java"
export BASE_DIR=$(dirname $0)/..
export CLASSPATH=".:${BASE_DIR}/lib/*:${BASE_DIR}/conf/*"

JAVA_OPT="-Djava.ext.dirs=${JAVA_HOME}/jre/lib/ext:${BASE_DIR}/lib/*"
JAVA_OPT="${JAVA_OPT} ${JAVA_OPT_EXT}"
JAVA_OPT="${JAVA_OPT} -cp ${CLASSPATH}"

$JAVA "${JAVA_OPT}" com.shallowinggg.narep.admin.NarepStarter $@