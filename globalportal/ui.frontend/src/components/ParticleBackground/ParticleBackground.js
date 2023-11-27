import React, { Component, useCallback } from "react";
import Particles from "react-tsparticles";
import ParticleConfig from "./Config/ParticleConfig";
import { loadFull } from "tsparticles";
import { MapTo } from "@adobe/aem-spa-component-mapping";



const ParticleEditConfig = {
    emptyLabel: "particle",
  
    isEmpty: function (props) {
      return !props;
    },
  };
function ParticleBackground() {
    const particlesInit = useCallback(async engine => {
        await loadFull(engine);
    }, []);

    const particlesLoaded = useCallback(async container => {
        await console.log(container);
    }, []);
    return (
        <Particles id="tsparticles"
        init={particlesInit}
        loaded={particlesLoaded}
        options={ParticleConfig}></Particles>
    )
}
export  class Particle extends Component {
    get content() {
        return (
            <ParticleBackground/>
        )

    }
    render() {   
        return (
<>
                {this.content}
</>
        );
    }
}
MapTo('globalportal/components/particle')(Particle,ParticleEditConfig)