const imageDom = document.getElementsByTagName("video")
for (var i = 0; i < imageDom.length; i++) {
    const width = imageDom[i].getAttribute('width');
    const height = imageDom[i].getAttribute('height');
    const element = imageDom[i];
    if(width>0 || height>0){
    element.style.width = width + "%";
    element.style.height = height + "%";
    }
}
