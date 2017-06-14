export TERM=$(awk -v RS=' ' -v FS='=' '$1 == "term" {print $2}' < /proc/cmdline)
tput init
stty rows $(awk -v RS=' ' -v FS='=' '$1 == "tty_lines" {print $2}' < /proc/cmdline)
stty cols $(awk -v RS=' ' -v FS='=' '$1 == "tty_cols"  {print $2}' < /proc/cmdline)
kill -s WINCH $$
