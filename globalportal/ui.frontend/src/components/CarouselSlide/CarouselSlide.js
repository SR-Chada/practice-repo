import React, {Component} from 'react';
import {MapTo} from '@adobe/aem-react-editable-components';
import { Image } from '../Image/Image';

export const CarouselSlideEditConfig = {
    emptyLabel: 'Hero Carousel Slide',

    isEmpty: function(props) {
        return !props || !props.src || props.src.trim().length < 1;
    }
};

export default class CarouselSlide extends Component {
    
    render() {
        const carouselIndicators = document.getElementsByClassName("cmp-carousel__indicators");
        for(var i=0; i < carouselIndicators.length; i++ ){
            if(carouselIndicators[0].children.length > 1){
                carouselIndicators[0].style.display = "inline-flex";
            }
        }
        return (
            <div>
              <Image src={this.props.src}
                    alt={this.props.alt} 
                    srcSet={this.props.srcSet}
                />
                <div className='cmp-carousal-slide-heading'>
                    <p className='cmp-carousal-slide-heading-level-one'>{ this.props.levelOneTitle}</p>
                    <p className='cmp-carousal-slide-heading-level-two'>{this.props.levelTwoTitle}</p>
                    <div className='cmp-carousal-slide-icon'>
                    
                        <div className='cmp-carousal-icon'> <div class="circle"></div></div>
                        <p className='cmp-heading-scroll-text'>Scroll</p>
                    </div>
                    
                   
                </div>
            </div>
           
        );
    }
                
}

MapTo('globalportal/components/herocarouselslide')(CarouselSlide, CarouselSlideEditConfig);
