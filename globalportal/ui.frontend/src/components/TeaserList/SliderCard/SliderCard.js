// //Imports

import { useEffect, useRef, useState } from "react";
import { Button } from "../../Button/Button";
import { Image } from "../../Image/Image";

require("./SliderCard.scss");

export const SliderCard = (props) => {
  const scrollRef = useRef(null);
  const scrolltoId = (i) => {
    const actualIndex = i;
    let access = scrollRef.current;
    access.scrollTo({
      top: access.scrollTop,
      left:
        localStorage.getItem("currentSlide") > i + 1
          ? -(access.scrollLeft + actualIndex * 580)
          : access.scrollLeft + actualIndex * 580,
      behavior: "smooth",
    });
  };
  useEffect(() => {
    localStorage.setItem("currentSlide", 1);
  }, []);

  return (
    <>
      <div className="snap-carousel" ref={scrollRef}>
        <ul>
          {props.item.map((item, i) => {
            let featuredImageAlt =
              item?.featuredImage &&
              (!item?.featuredImage?.alt ? "" : item?.featuredImage?.alt);
            let featuredImageFile =
              item?.featuredImage &&
              (!item?.featuredImage?.fileReference
                ? ""
                : item?.featuredImage?.fileReference);
            return (
              <li key={i} id={item?.name}>
                <div className=" cq-dd-image">
                  <div className="cmp-teaser__image">
                    <Image src={featuredImageFile} alt={featuredImageAlt} />
                  </div>
                  <div className="cmp-teaser__content">
                    <div className="cmp-teaser__title">
                      <p className="cmp-teaser__sub_title">
                        {" "}
                        {item.pageSubtitle}
                      </p>
                      <div>
                        <span className="cmp-teaser__arrow"></span>

                        <h3 className="cmp-teaser__title-text">
                          <a
                            className="cmp-teaser__title-link"
                            href={item.link.url}
                          >
                            {item.title}
                          </a>
                        </h3>
                      </div>
                    </div>
                    <div className="cmp-teaser__description">
                      {item.description}
                    </div>
                    <div className="cmp-button-outside-arrow">
                      <Button
                        text={props.readMoreButtonText}
                        buttonLink={item.link}
                      />
                    </div>
                  </div>
                </div>
                {props.item.length - 1 === i && (
                  <div className="cmp-button-inside-arrow">
                    <Button
                      text={"Technology & Intelligence"}
                      buttonLink={{
                        url: "content/macnicagwi/globalportal/en/technology-intelligence.html",
                      }}
                    />
                  </div>
                )}
              </li>
            );
          })}
        </ul>
      </div>
      <ul className="slick-dots">
        {props.item.map((_, i) => {
          return (
            <li
              key={i}
              id={"item" + i}
              className={i==0 && "slick-active"}
              onClick={() => {
                scrolltoId(i);
                localStorage.setItem("currentSlide", i + 1);
                let element = document.getElementById("item" + i);
                element.classList.remove("slick-active");
                if (i == parseInt(localStorage.getItem("currentSlide")) - 1) {
                  element.classList.add("slick-active");
                }
                let elements = document.getElementsByClassName("slick-active");
                const otherElements = [...elements].filter(item=>item.id != "item"+i)
                otherElements.map(item=>item.classList.remove("slick-active"))
              }}
            />
          );
        })}
      </ul>
    </>
  );
};
