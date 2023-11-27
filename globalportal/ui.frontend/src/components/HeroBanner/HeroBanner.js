//Imports
import React, { Component } from 'react';
import { MapTo } from '@adobe/aem-react-editable-components';
require("./HeroBanner.scss");
//Define EditConfig object
/**
 * Default Edit configuration for the CustomTextComponent component
 *
 * @type EditConfig
 */
const HeroBannerComponentEditConfig = {
  emptyLabel: "Hero Banner",
  isEmpty: function (props) {
    return !props || !props.title || !props.src;
  },
};

//Define the React.js component
/**
 * CustomTextComponent React component
 */
class HeroBannerComponent extends Component {
  render() {
    return (
      <div className="cmp-herobanner">
        <img
          className="cmp-herobanner-image"
          srcset={this.props.srcset}
          src={this.props.src}
          alt={this.props.alt}
          sizes="(max-width: 375px) 375px,(max-width: 768px) 768px, 1440px"
        ></img>
        <div className="cmp-herobanner__title">
            <h1 className="cmp-herobanner-title">{this.props.title}</h1>
        </div>
      </div>
    );
  }
}

//Map the react component to the AEM component using its sling:resourceType
export default MapTo('globalportal/components/herobanner')(
    HeroBannerComponent,HeroBannerComponentEditConfig
);

