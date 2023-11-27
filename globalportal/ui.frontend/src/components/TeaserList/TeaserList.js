import React, { Component } from "react";

import { MapTo } from "@adobe/aem-react-editable-components";

import { Button } from "../Button/Button";

import { Image } from "../Image/Image";
import CardComponent from "./Card/Card";
import { SliderCard } from "./SliderCard/SliderCard";
import { Link } from "react-router-dom";

require("./TeaserList.scss");

export const TeaserListEditConfig = {
  emptyLabel: "Teaser List",

  isEmpty: function (props) {
    return props.items.length === 0;
  },
};
class TeaserList extends Component {
  basicClass = `cq-dd-image`;
  attachedClass = ` cmp-teaser-variation-one-bg-white`;
  propItemsLength = this.props?.items ? this.props?.items?.length : 0;
  isOdd(num) {
    return !(num % 2) ? this.basicClass + this.attachedClass : this.basicClass;
  }

  get Content() {
    if (this.props.centerOfExcellenceTeaser) {
      return (
        <SliderCard
          item={this.props.items}
          readMoreButtonText={this.props.readMoreButtonText}
        />
      );
    } else if (this.props.solutionListingTeaser || this.props.services) {
      return (
        <>
          <CardComponent
            item={this.props.items}
            readMoreButtonText={this.props.readMoreButtonText}
            solutionListingTeaser={this.props.solutionListingTeaser}
            services={this.props.services}
            parentPath={this.props.parentPageLink.url}
          />
        </>
      );
    } else {
      const styles = {
        background: this.props.backGroundColor == "true" ? "#FAF7FA" : "",
      };

      return (
        <div className="teaser-outer" style={styles}>
          {" "}
          {this.props.items.map((item, i) => {
            let featuredImageAlt =
              item?.featuredImage &&
              (!item?.featuredImage?.alt ? "" : item?.featuredImage?.alt);
            let featuredImageFile =
              item?.featuredImage &&
              (!item?.featuredImage?.fileReference
                ? ""
                : item?.featuredImage?.fileReference);
            let featuredImageSrcSet =
              item?.featuredImage &&
              (!item?.featuredImage?.srcset ? "" : item?.featuredImage?.srcset);

            if (this.props.anchorTagLink == 'true') {
              return (
                <div className={`${this.isOdd(i)}`}>



                  <div
                    className={`teaser-inner-container ${i === 0 ? "pt0" : ""} ${i === this.propItemsLength - 1 && this.isOdd(this.propItemsLength - 1) !== this.basicClass ? "pb0" : ""
                      }`}
                  >
                    <div className="cmp-teaser__image">
                      <Image
                        src={featuredImageFile}
                        alt={featuredImageAlt}
                        imageLink={item.link}
                      />
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

                          {(item?.link?.url).includes("https" || "http") ? (
                              <a
                                href={item?.link?.url}
                                className="cmp-teaser__title-link"
                                target="_blank"
                              >
                                {item.teaserTitle ? item.teaserTitle : item.title}
                              </a>
                            ) : (
                              <Link
                                to={item?.link?.url}
                                className="cmp-teaser__title-link" onClick={() => {
                                  document.title = item.title
                                }}>
                                {item.teaserTitle ? item.teaserTitle : item.title}
                              </Link>
                            )}

                          </h3>
                        </div>
                      </div>
                      <div className="cmp-teaser__description">
                        {item.description}
                      </div>
                      <div className="cmp-button-inside-arrow">
                      <div onClick={() => {
                         document.title = item.title
                       }}>
                        <Button
                          text={this.props.readMoreButtonText}
                          buttonLink={item.link}
                        />
                        </div>
                      </div>
                    </div>
                  </div>

                </div>
              );
            }
            else {
              return (
                <div className={`${this.isOdd(i)}`}>
                  <div
                    className={`teaser-inner-container ${i === 0 ? "pt0" : ""} ${i === this.propItemsLength - 1 && this.isOdd(this.propItemsLength - 1) !== this.basicClass ? "pb0" : ""
                      }`}
                  >
                    <div className="cmp-teaser__image">
                      <Image
                        src={featuredImageFile}
                        alt={featuredImageAlt}
                        imageLink={item.link}
                      />
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
                          {(item?.link?.url).includes("https" || "http") ? (
                              <a
                                href={item?.link?.url}
                                className="cmp-teaser__title-link"
                                target="_blank"
                              >
                                {item.teaserTitle ? item.teaserTitle : item.title}
                              </a>
                            ) : (
                              <Link
                                to={item?.link?.url}
                                className="cmp-teaser__title-link"  onClick={() => {
                                  document.title = item.title
                                }}>
                                {item.teaserTitle ? item.teaserTitle : item.title}
                              </Link>
                            )}

                          </h3>
                        </div>
                      </div>
                      <div className="cmp-teaser__description">
                        {item.description}
                      </div>
                      <div className="cmp-button-inside-arrow">
                      <div onClick={() => {
                         document.title = item.title
                       }}>
                        <Button
                          text={this.props.readMoreButtonText}
                          buttonLink={item.link}
                        />
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

              );
            }


          })}
        </div>
      );
    }
  }

  render() {
    return <> {this.Content}</>;
  }
}

MapTo("globalportal/components/teaserlist")(TeaserList, TeaserListEditConfig);
