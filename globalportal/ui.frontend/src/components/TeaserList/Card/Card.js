//Imports
import React, { Component } from 'react';
import { Button } from '../../Button/Button';
import { Image } from '../../Image/Image';
import { ServicesList } from '../ServicesCard/ServicesList';
import { Link } from 'react-router-dom';
require('./Card.scss');

export default class CardComponent extends Component {

    get content() {
        const buttonAttribute = {
            url: this.props.parentPath,
            attributes: {
                target: "_self",
            },
        }
        if (this.props.solutionListingTeaser ) {
          return  <>
            <div className="teaser-card-main">
          {this.props.item.map((item, i) => {
          let featuredImageAlt =
              item?.featuredImage &&
              (!item?.featuredImage?.alt ? "" : item?.featuredImage?.alt);
          let featuredImageFile =
              item?.featuredImage &&
              (!item?.featuredImage?.fileReference
                  ? ""
                  : item?.featuredImage?.fileReference);
          return (

                  <div className="teaser-card">
                  <Link to={item?.link?.url}>
                      <Image src={featuredImageFile}
                          alt={featuredImageAlt} />
                  <div className="teaser-card-content">
                      <img className="teaser-card-icon" src={item?.pageIcon?.iconFileReference == undefined ? "" : item.pageIcon.iconFileReference} alt={item.title} />
                      <p className="teaser-card-header">{item.title} </p>
                      </div>
                  </Link>
                  </div>

          );
     })}
     <div className="teaser-card last-card">
     <div  className="teaser-card last-card-inside">
                  <div className="cmp-button-inside-arrow">
                      <Button
                          text={this.props.readMoreButtonText}
                                  buttonLink={buttonAttribute} />
                  </div>
              </div>
              </div>
          </div>
     </>  
        } else if (this.props.services) {
           return <>
            <ServicesList {...this.props}/>
     </>
        }
       
    }
    render() {
        return (
            this.content
        )
        
    }
}

