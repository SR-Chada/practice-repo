import React, { Component } from "react";
import { MapTo } from "@adobe/aem-react-editable-components";
/* import {Link} from "react-router-dom"; */

require("./Title.scss");

const TitleEditConfig = {
  emptyLabel: "Title",

  isEmpty: function (props) {
    return !props || !props.text || props.text.trim().length < 1;
  },
};

export default class Title extends Component {
  get content() {
    const CustomTag = `${this.props.type}`;
    /* 
        return <CustomTag
                className="cmp-title__text"> {this.props.text} </CustomTag>; */
    if (this.props && this.props.link && this.props.text) {
      return (
        <CustomTag className="cmp-title__text" id={this.props.id}>
          <div className="cmp-title__text-content">
            <a href={this.props.link} className="cmp-title__action-link">
              {this.props.text}
            </a>
          </div>
        </CustomTag>
      );
    } else {
      if (CustomTag === "undefined") {
        return (
          <h1 className="cmp-title__text" id={this.props.id}>
            <div className="cmp-title__text-content">
              <a href={this.props.link} className="cmp-title__action-link">
                {this.props.text}
              </a>
            </div>
          </h1>
        );
      } else {
        return (
          <CustomTag className="cmp-title__text" id={this.props.id}>
            <div className="cmp-title__text-content">
              {this.props.pretitle && <p>{this.props.pretitle}</p>}
              <a href={this.props.link} className="cmp-title__action-link">
                {this.props.text}
              </a>
            </div>
          </CustomTag>
        );
      }
    }
  }

  render() {
    if (TitleEditConfig.isEmpty(this.props)) {
      return null;
    }

    return <div className="cmp-title">{this.content}</div>;
  }
}

MapTo("globalportal/components/title")(Title, TitleEditConfig);
