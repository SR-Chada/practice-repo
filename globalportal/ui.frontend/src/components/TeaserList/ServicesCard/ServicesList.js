
import React, { useEffect, useState } from "react";
import { Teaser } from "../../Teaser/Teaser";

export const ServicesList = (props) => {
    const [selector, setSelector] = useState({ ...props?.item[0], count: "0" + (0 + 1) });
    const [idSelector, setIdSelector] = useState("teaser--card_0");
    const onMouseEnter = (i) => {
        document.getElementById(idSelector).classList.remove("active_card");
        setIdSelector("teaser--card_" + i)
        let object = { ...props?.item[i], count: "0" + (i + 1) }
        setSelector(object);
    };
    useEffect(() => {
        document.getElementById(idSelector).classList.add("active_card");
    });
    let actions = [{ title: "Read more", link: { url: selector.link.url } }]
    return <>
        <div className="teaser-card-main main-services">
            {props.item.map((item, i) => {
                return (
                    <div key={i} onClick={() => onMouseEnter(i)} id={"teaser--card_" + i} className="teaser-card teaser-card-services">
                        <div className="teaser-card-header"><span>{"0" + (i + 1)}</span>
                            <img className="teaser-card-icon" src={item?.pageIcon?.iconFileReference === undefined ? "" : item.pageIcon.iconFileReference} alt={item.title} />
                            <p>{item.title} </p>
                            <div className="teaser-card-arrow-icon" />
                        </div>
                    </div>
                );
            })}


        </div>
        <div className='cmp-global-teaser-variation cmp-global-teaser-variation-one '>
            <Teaser
                pretitle={selector.count}
                title={selector.title}
                description={selector.description}
                titleType={selector.titleType}
                linkURL={selector.linkURL}
                actionsEnabled={true}
                imageLinkHidden={selector.imageLinkHidden}
                imageAlt={selector.featuredImage.fileReference}
                titleLinkHidden={selector.titleLinkHidden}
                actions={actions || []}
                imagePath={selector.featuredImage.fileReference} />
        </div>
    </>
}