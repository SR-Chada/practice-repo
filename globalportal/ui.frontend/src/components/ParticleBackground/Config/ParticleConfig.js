const paticleJSON = 
{
 background: {
     color: {
         value: "#ffffff",
     },
 },
 fpsLimit: 120,
 interactivity: {
detect_on: "canvas",
events: {
onhover: {
enable: false,
mode: "repulse"
},
onclick: {
enable: false,
mode: "push"
},
resize: true
},
modes: {
grab: {
distance: 400,
line_linked: {
 opacity: 1
}
},
bubble: {
distance: 280,
size: 37,
duration: 2,
opacity: 8,
speed: 3
},
repulse: {
distance: 200,
duration: 0.4
},
push: {
particles_nb: 4
},
remove: {
particles_nb: 2
}
}
},
 particles: {
number: {
value: 65,
density: {
enable: true,
value_area: 800
}
},
color: {
value: "#7f1084"
},
shape: {
type: "circle",
stroke: {
width: 0,
color: "#ffffff"
},
polygon: {
nb_sides: 5
}
},
opacity: {
value: 0.5,
random: false,
anim: {
enable: false,
speed: 1,
opacity_min: 0.1,
sync: false
}
},
size: {
value: 1.5,
random: true,
anim: {
enable: false,
speed: 32,
size_min: 1,
sync: true
}
},
line_linked: {
enable: true,
distance: 140,
color: "#7f1084",
opacity: 0.4,
width: 0.8
},
move: {
enable: true,
speed: 6,
direction: "top",
random: true,
straight: false,
out_mode: "out",
bounce: false,
attract: {
enable: false,
rotateX: 600,
rotateY: 1200
}
}
},
 detectRetina: true,
}

export default paticleJSON;