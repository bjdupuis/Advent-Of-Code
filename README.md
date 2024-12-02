# Advent Of Code

Based on Hugh Davie's [aoc-kotlin-starter](https://github.com/hughjdavey/aoc-kotlin-starter).

Note that per AoC's recommendation, input files are not stored in the repo. They're expected to be found in `src/main/resources/<year>/input_day_<day>.txt`.

## Creating the daily templates

Added a utility to create the files for a particular day. Create a run configuration that runs this command.

`run --args "today" -Pmain="util.ScaffoldCreator"`

## Run the current day

Create a run configuration like this.

`run --args "today" -Pmain="util.Runner"`