import React, { Component, useEffect, useRef, useState } from "react";
import { MapTo } from "@adobe/aem-react-editable-components";
import { TeaserV1, TeaserV1IsEmptyFn } from "@adobe/aem-core-components-react-base";
import { Link } from "react-router-dom";

require('./Teaser.scss');


export const Teaser = (props) => {
    const teaserRef = useRef(null);
    const [isIntersecting, setIsIntersecting] = useState(false);
    const callBackFunction = (entries) => {
        const [entry] = entries;
        setIsIntersecting(entry.isIntersecting);
        if (entry.isIntersecting) {
            setTimeout(function () {
                document.querySelector('.cmp-global-teaser-variation-zoom-teaser .zoom-teaser').classList.add('zoom-teaser-hide');
            }, 2000)
        } else {
            document.querySelector('.cmp-global-teaser-variation-zoom-teaser .zoom-teaser').classList.remove('zoom-teaser-hide');

        }
    }
    const options = {
        root: null,
        rootMargin: "0px",
        threshold: 0.1
    }
    useEffect(() => {
        const arr = document.getElementsByClassName("cmp-teaser__action-link");
        const arr1 = document?.getElementsByClassName("cmp-global-teaser-default");
        
        for (let index = 0; index < arr.length; index++) {
            const replacedDollor = arr[index].innerText.replace("$", "");
            arr[index].innerHTML = replacedDollor;
        }
        const Observer = new IntersectionObserver(callBackFunction, options)
        if (teaserRef.current) {
            Observer.observe(teaserRef.current);

        }
        for (let index = 0; index < arr1.length; index++) {
            const DDImageCont = arr1[index].firstChild;
            const imageCont = DDImageCont.firstChild;
            const contentCont = DDImageCont.lastChild;
            const titleDiv =
              contentCont?.getElementsByClassName("cmp-teaser__title");

              if(titleDiv != undefined && titleDiv.length > 0){
                imageCont.style.paddingTop = titleDiv[0]?.clientHeight + 50 + "px";
              }
              

            
          }
    }, [teaserRef, options]
    )
    let actions = props.actions && props.actions.map((items) => {

            return { title: items.title, URL: items.link.url }
    })
    if (props.id == "zoom-effect") {
        return (<>
            <div className="zoom-teaser" ref={
                teaserRef
            }>
                <span class="top" style={{
                    transform: isIntersecting ? 'scale(1,0)' : 'scale(1,1)',
                    transition: 'transform 3s ease-in-out',
                }}></span>
                <span class="bottom" style={{
                    transform: isIntersecting ? 'scale(1,0)' : 'scale(1,1)',
                    transition: 'transform 3s ease-in-out',
                }}></span>
                <span class="right" style={{
                    transform: isIntersecting ? 'scale(0,1)' : 'scale(1,1)',
                    transition: 'transform 3s ease-in-out',
                }}></span>
                <span class="left" style={{
                    transform: isIntersecting ? 'scale(0,1)' : 'scale(1,1)',
                    transition: 'transform 3s ease-in-out',
                }}></span>
            </div>
            <div onClick={() => {
              setTimeout(() => {
                   document.title = document.getElementsByClassName("cmp-breadcrumb__item--active")[0].outerText;
               }, 200);
             }}>
            <TeaserV1
                pretitle={props.pretitle}
                title={props.title}
                description={props.description}
                titleType={props.titleType}
                linkURL={props?.link?.url}
                actionsEnabled={props.actionsEnabled}
                imageLinkHidden={props.imageLinkHidden}
                imageAlt={props.imageAlt}
                titleLinkHidden={props.titleLinkHidden}
                actions={actions || []}
                imagePath={props.imagePath} /></div></>);
    } else {
        return (<div onClick={() => {
            setTimeout(() => {
                document.title = document.getElementsByClassName("cmp-breadcrumb__item--active")[0].outerText;
            }, 200);
          }}><TeaserV1
            pretitle={props.pretitle}
            title={props.title}
            description={props.description}
            titleType={props.titleType}
            linkURL={props?.link?.url}
            actionsEnabled={props.actionsEnabled}
            imageLinkHidden={props.imageLinkHidden}
            imageAlt={props.imageAlt}
            titleLinkHidden={props.titleLinkHidden}
            actions={actions || []}
            imagePath={props.imagePath} 
             /></div>)
    }
    }



MapTo("globalportal/components/teaser")(Teaser, TeaserV1IsEmptyFn)