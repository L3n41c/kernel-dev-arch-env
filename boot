#!/bin/bash
set -euo pipefail

drive=mkosi.output/image.qcow2
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

exec "qemu-system-$(uname -m)" \
    -name kernel-dev-arch-env \
    -machine q35,accel=kvm,usb=off,vmport=off \
    -cpu host \
    -smp 2 \
    -m 2048 \
    -kernel "$kernel" \
    -device virtio-scsi-pci \
    -drive file="$drive",format=qcow2,if=none,id=rootdisk,discard=unmap \
    -device scsi-hd,drive=rootdisk \
    -append "root=/dev/sda1 rootflags=autodefrag,discard rw init=/usr/lib/systemd/systemd console=hvc0 term=$TERM tty_lines=$(tput lines) tty_cols=$(tput cols)" \
    -fsdev local,id=fsdev-fs0,path=$HOME,security_model=none,readonly \
    -device virtio-9p-pci,fsdev=fsdev-fs0,mount_tag=host_home \
    -chardev stdio,id=stdio,mux=on,signal=off \
    -device virtio-serial-pci \
    -device virtconsole,chardev=stdio \
    -mon chardev=stdio \
    -net nic,model=virtio -net user \
    -device virtio-balloon-pci \
    -object rng-random,id=objrng0,filename=/dev/urandom \
    -device virtio-rng-pci,rng=objrng0 \
    -device pvpanic \
    -display none \
    -s
