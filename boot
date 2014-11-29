#!/bin/bash

set -e

drive=arch.img
kernel=vmlinuz

usage()
{
    cat <<EOF
Usage: boot [-k kernel] [-d drive]

  -k kernel   Path to the kernel. Default to $kernel.
  -d drive    Path to the userland drive. Default to $drive.
EOF
}

while getopts hd:k: opt; do
    case "$opt" in
        k)
            kernel="$OPTARG"
            ;;
        d)
            drive="$OPTARG"
            ;;
        h)
            usage
            exit 0
            ;;
        ?)
            usage
            exit 1
            ;;
    esac
done

exec qemu-system-$(uname -m) \
    -machine pc,accel=kvm,usb=off \
    -smp 2 \
    -m 2048 \
    -kernel "$kernel" \
    -drive file="$drive",if=virtio \
    -append "root=/dev/vda rw init=/usr/lib/systemd/systemd console=hvc0" \
    -chardev stdio,id=stdio,mux=on \
    -device virtio-serial-pci \
    -device virtconsole,chardev=stdio \
    -mon chardev=stdio \
    -net nic,model=virtio -net user \
    -balloon virtio \
    -display none \
    -nographic \
    -s