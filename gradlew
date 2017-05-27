#!/usr/bin/python

from __future__ import print_function

import subprocess


# This prefix is necessary to prevent Docker from buffering Python subprocess
# output to pipe. This is required to e.g. enable continuous monitoring of
# unit test or integration test execution.
UNBUFFER_PREFIX = ["stdbuf", "-oL", "-eL"]


class CommandException(subprocess.CalledProcessError):
    """Exception which is raised if the command fails to execute."""

    def __str__(self):
        return "Command '{}' returned non-zero exit status {}".format(
            " ".join(self.cmd), self.returncode)


def _run_command(command):
    """Continuously supply output from a child process."""

    # Please note - joining stdout and stderr is a must since tools
    # like pep8, pylint and mvn write errors to STDOUT and not STDERR.
    # This leads us to really polluted exceptions in case of failures,
    # but unfortunately there is nothing that can be done about it.
    process = subprocess.Popen(command,
                               stdout=subprocess.PIPE,
                               stderr=subprocess.STDOUT)

    for line in iter(process.stdout.readline, ""):
        yield line

    process.stdout.close()

    status = process.wait()

    if status:
        raise CommandException(status, command)


def _always(flag):
    return lambda line: flag


def run_command(command, silent=False, printable=None):
    """
    Execute a command, print output to stdout line by line and return the whole
    output as a string.

    :param command: shell statements to execute
    :type command: list
    :param silent: if True - output is not printed on the screen
    :type silent: bool
    :param printable: a function that takes a line as input and returns boolean
                      denoting whether this line should be printed or not.
                      True if it should be printed. False otherwise.
    :type printable: lambda line: True/False
    :return: output of the command
    :rtype: str
    :raises: CommandException if the status code returned by the command is > 0
    """
    lines = []

    def _msg():
        return "".join(lines).rstrip("\n")

    if silent:
        printable = _always(False)

    if not printable:
        printable = _always(True)

    try:
        for line in _run_command(UNBUFFER_PREFIX + command):
            lines.append(line)
            if printable(line):
                print(line, end="")
    except CommandException as error:
        raise CommandException(error.returncode, command, _msg())
    return _msg()


IGNORE_CHUNKS = [
    'NDK is missing a "platforms" directory.',
    'If you are using NDK, verify the ndk.dir is set to a valid NDK directory.',
    'If you are not using NDK, unset the NDK variable from ANDROID_NDK_HOME',
    'classpath entry points to a non-existent location'
]


def printable(line):
    if not line.strip():
        return False
    for chunk in IGNORE_CHUNKS:
        if chunk in line:
            return False
    return True

import sys

params = sys.argv[1:]

run_command(["bash", "gradlew.orig"] + params, printable=printable)
