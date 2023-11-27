const imageDom = document.querySelectorAll('.cmp-resizableimage__image');
for (var i = 0; i < imageDom.length; i++) {
    const width = imageDom[i].getAttribute('width');
    const height = imageDom[i].getAttribute('height');
    const element = imageDom[i];
    element.style.width = width + "px";
    element.style.height = height + "px";
}
