import React, { Component } from "react";
import { MapTo } from "@adobe/aem-react-editable-components";
import { Link } from "react-router-dom";

export const ImageEditConfig = {
  emptyLabel: "Image",

  isEmpty: function (props) {
    return !props || !props.src || props.src.trim().length < 1;
  },
};

export class Image extends Component {
  get content() {
    let isExternal = /^https?:\/\//.test(this.props?.imageLink?.url);
    return this.props?.imageLink?.url ? (
      isExternal === true ?
        <a
          href={this.props?.imageLink?.url}
          target="_blank"
          rel="noopener noreferrer"
        >
          <img
            id={this.props.id}
            className="cmp-image__image"
            srcset={this.props.srcset}
            src={this.props.src}
            alt={this.props.alt}
            title={this.props.title ? this.props.title : this.props.alt}
            sizes="(max-width: 375px) 375px,(max-width: 768px) 768px, 1440px"
          />
        </a> :
        <Link to={this.props?.imageLink?.url} title={this.props.alt} onClick={() => {
          setTimeout(() => {
            if (window.location.href.indexOf("home") > -1) {
              document.title = "Home";
            }
          }, 200);
         }} >
          <img
            id={this.props.id}
            className="cmp-image__image"
            srcset={this.props.srcset}
            src={this.props.src}
            alt={this.props.alt}
            title={this.props.title ? this.props.title : this.props.alt}
            sizes="(max-width: 375px) 375px,(max-width: 768px) 768px, 1440px"
          />
        </Link>
    ) : (
      <img
        id={this.props.id}
        className="cmp-image__image"
        srcset={this.props.srcset}
        src={this.props.src}
        alt={this.props.alt}
        title={this.props.title ? this.props.title : this.props.alt}
        sizes="(max-width: 375px) 375px,(max-width: 768px) 768px, 1440px"
      />
    );
  }

  render() {
    if (ImageEditConfig.isEmpty(this.props)) {
      return null;
    }

    return <div className="cmp-image">{this.content}</div>;
  }
}

MapTo("globalportal/components/image")(Image, ImageEditConfig);
