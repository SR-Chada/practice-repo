package com.macnicagwi.core.services;

import java.util.List;

import javax.jcr.Session;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.day.cq.dam.api.Asset;

/**
 * DAM Asset Service
 * <p>
 * This service allows users to perform DAM Assets related implemnetations. 
 * @author Sumit Kumar Agarwal
 */
public interface DamAssetService {

    List<ContentFragment> getFilteredContentFragments(List<String> filters, String filterProperty, String cfmParentFolderPath, Session session);

    List<Asset> getProductLineLogos(List<String> productLines, String assetProductLineProperty, String assetParentFolderPath, Session session);

    List<Asset> getAssetByDamSha(String assetDamSha, String damShaProperty, String assetParentFolderPath, Session session);

}

