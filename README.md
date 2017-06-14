# Kernel development environment (Arch)

This project is a set of scripts to use to create a minimal kernel development environment.

## Kernel config

```
cd <kernel source dir>
make allnoconfig
scripts/kconfig/merge_config.sh -m .config <kernel-dev-arch-env dir>/kernel-dev.config
make kvmconfig
make -j $(nproc)
```

## Create a drive containing a minimal userland

```
su
mkosi
chown $USER arch.raw
```

## Boot the kernel

```
./boot -k <kernel source dir>/arch/x86_64/boot/bzImage
```
