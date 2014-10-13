# Kernel development environment (Arch)

This project is a set of scripts to use to create a minimal kernel development environment.

## Kernel config

* Copy the `config` file in the kernel tree
* `make oldconfig`
* `make -j 12`

## Create a drive containing a minimal userland

* `su -c "./create_drive -u $USER"`

## Boot the kernel

* `./boot -k <kernel source dir>/arch/x86_64/boot/bzImage`
