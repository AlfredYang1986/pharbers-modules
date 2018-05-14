package com.pharbers.builder

import com.pharbers.builder.search._
import com.pharbers.builder.mapping.marketMapping

class SearchFacade extends marketMapping with SearchAllMktTrait with SearchHistory
        with SearchSimpleCheckSelect with SearchSimpleCheck with SearchResultCheck
