import React, {Component} from 'react';
import {MapTo} from '@adobe/aem-react-editable-components';
import {Link} from "react-router-dom";
import './Button.scss';

export const ButtonEditConfig = {

    emptyLabel: 'Button',

    isEmpty: function(props) {
        return !props || !props.text || props.text.trim().length < 1;
    }
};

export class Button extends Component {
  scrollToTop = (_) => {
    window.scrollTo({
      top: 0,
      left: 0,
      behavior: "smooth",
    });
  };
  onClickTop = () => {
    if (this.props.id === "scrollTop") {
      return () => this.scrollToTop();
    }
  };
  get buttonContent() {
    let buttonLink = this.props.buttonLink !== undefined ? this.props.buttonLink.url : "";
    let isExternal = /^https?:\/\//.test(buttonLink);

    if (this.props && this.props.buttonLink && this.props.text && !isExternal ) {
      return (
        <Link
          to={this.props.buttonLink.url}
          className="cmp-button cmp-button__action-link"
          id={this.props.id}
          onClick={this.scrollToTop()}
          target={this.props.buttonLink?.attributes?.target}
        >
          <span className="cmp-button__text"> {this.props.text}</span>
        </Link>
      );
    } else if (this.props && this.props.buttonLink && this.props.text && isExternal) {
      return (
        <a
          href={this.props.buttonLink.url}
          className="cmp-button cmp-button__action-link"
          id={this.props.id}
          onClick={this.scrollToTop()}
          target={this.props.buttonLink?.attributes?.target}
        >
          <span className="cmp-button__text"> {this.props.text}</span>
        </a>
      );
    } else {
      return (
        <button
          className="cmp-button"
          id={this.props.id}
          onClick={this.onClickTop()}
        >
          <span className="cmp-button__text"> {this.props.text} </span>
        </button>
      );
    }
  }

  render() {
    if (ButtonEditConfig.isEmpty(this.props)) {
      return null;
    }

    return <div className="button">{this.buttonContent}</div>;
  }
}


MapTo('globalportal/components/button')(Button, ButtonEditConfig);