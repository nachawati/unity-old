<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<div class="modal-content">
	<div class="modal-header bg-purple">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="modalLabel">
			<i class="octicon octicon-zap mr-5"></i>Results
		</h4>
	</div>
	<div class="modal-body">
		<div class="row">
			<div class="col-md-12">
				
    			
    				<div class="container-fluid" >
    					<input type="hidden" id="new-content1" name="content" value="">
    					<div id="form-view1" style="display: none;"></div>
    				</div>
				<div id="results">
					<div style="padding: 100px; text-align: center;">
						<i class="fa fa-spinner fa-spin fa-5x fa-fw"></i>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	</div>
</div>
<script>
	$.ajax({
		url : "${pageContext.request.contextPath}/${it.path}/run?path=${pageContext.request.getParameter('path')}",
		method : "POST",
		data : editor != null ? JSON.stringify(editor.getValue()) : JSON.stringify(form.getValue()),
		success : function(data) {
			
			
			$("#form-view1").css("display", "block");
			$("#results").css("display", "none");
			try {
				JSONEditor.defaults.theme = 'bootstrap3';
				JSONEditor.defaults.iconlib = 'fontawesome4';
			
				var form = new JSONEditor(document.getElementById("form-view1"),
						{
							ajax: true,
							schema: {
							    "$schema": "http://json-schema.org/draft-04/schema#",
							    "definitions": {
							        "distribution": {
							            "type": "object",
							            "oneOf": [{
							                "title": "Uniform distribution",
							                "properties": {
							                    "distr": {
							                        "type": "string",
							                        "enum": ["uniform"]
							                    },
							                    "min": {
							                        "type": "number",
							                        "minimum": 0
							                    },
							                    "max": {
							                        "type": "number",
							                        "minimum": 0
							                    }
							                },
							                "required": ["distr", "min", "max"]
							            }, {
							                "title": "Normal distribution",
							                "properties": {
							                    "distr": {
							                        "type": "string",
							                        "enum": ["normal"]
							                    },
							                    "mean": {
							                        "type": "number",
							                        "minimum": 0
							                    },
							                    "sigma": {
							                        "type": "number",
							                        "minimum": 0
							                    }
							                },
							                "required": ["distr", "mean", "sigma"]
							            }]
							        },
							        "lbub": {
							            "type": "number",
							            "minimum": 0
							        },
							        "integerParamAnnotatedObj": {
							            "type": "object",
							            "properties": {
							                "v": {
							                    "oneOf": [{
							                        "title": "Integer value",
							                        "type": "integer",
							                        "minimum": 0
							                    }, {
							                        "type": "object",
							                        "title": "Annotated integer parameter",
							                        "properties": {
							                            "integer...": {
							                                "type": ["string", "null"]
							                            }
							                        },
							                        "required": ["integer..."]
							                    }]
							                },
							                "lb": {
							                    "$ref": "#/definitions/lbub"
							                },
							                "ub": {
							                    "$ref": "#/definitions/lbub"
							                },
							                "distribution": {
							                    "$ref": "#/definitions/distribution"
							                }
							            },
							            "required": ["v"]
							        },
							        "decimalParamAnnotatedObj": {
							            "type": "object",
							            "properties": {
							                "v": {
							                    "oneOf": [{
							                        "title": "Decimal value",
							                        "type": "number",
							                        "minimum": 0
							                    }, {
							                        "type": "object",
							                        "title": "Annotated decimal parameter",
							                        "properties": {
							                            "decimal...": {
							                                "type": ["string", "null"]
							                            }
							                        },
							                        "required": ["decimal..."]
							                    }]
							                },
							                "lb": {
							                    "$ref": "#/definitions/lbub"
							                },
							                "ub": {
							                    "$ref": "#/definitions/lbub"
							                }
							            },
							            "required": ["v"]
							        },
							        "integerVarAnnotatedObj": {
							            "type": "object",
							            "properties": {
							                "v": {
							                    "oneOf": [{
							                        "title": "Integer value",
							                        "type": "integer",
							                        "minimum": 0
							                    }, {
							                        "type": "object",
							                        "title": "Annotated integer variable",
							                        "properties": {
							                            "integer?": {
							                                "type": ["string", "null"]
							                            }
							                        },
							                        "required": ["integer?"]
							                    }]
							                },
							                "lb": {
							                    "$ref": "#/definitions/lbub"
							                },
							                "ub": {
							                    "$ref": "#/definitions/lbub"
							                },
							                "distribution": {
							                    "$ref": "#/definitions/distribution"
							                }
							            },
							            "required": ["v"]
							        },
							        "decimalVarAnnotatedObj": {
							            "type": "object",
							            "properties": {
							                "v": {
							                    "oneOf": [{
							                        "title": "Decimal value",
							                        "type": "number",
							                        "minimum": 0
							                    }, {
							                        "type": "object",
							                        "title": "Annotated decimal variable",
							                        "properties": {
							                            "decimal?": {
							                                "type": ["string", "null"]
							                            }
							                        },
							                        "required": ["decimal?"]
							                    }]
							                },
							                "lb": {
							                    "$ref": "#/definitions/lbub"
							                },
							                "ub": {
							                    "$ref": "#/definitions/lbub"
							                }
							            },
							            "required": ["v"]
							        }
							    },
							    "id": "http://repository.vsnet.gmu.edu/process/mfgSupplyChain/composite/supplyChain/lib/schema/variable_annotated",
							    "type": "object",
							    "properties": {
							        "config": {
							            "type": "object",
							            "properties": {
							                "intervalLength": {
							                    "type": "object",
							                    "properties": {
							                        "v": {
							                            "type": "number",
							                            "minimum": 0,
							                            "exclusiveMinimum": true
							                        },
							                        "unit": {
							                            "type": "string",
							                            "enum": ["sec"]
							                        }
							                    },
							                    "required": ["v", "unit"]
							                }
							            },
							            "required": ["intervalLength"]
							        },
							        "input": {
							            "type": "object",
							            "properties": {
							                "root": {
							                    "type": "string",
							                    "pattern": "^[A-Za-z0-9-_/#\\s]+$"
							                },
							                "kb": {
							                    "type": "object",
							                    "patternProperties": {
							                        "^[A-Za-z0-9-_/#\\s]+$": {
							                            "type": "object",
							                            "oneOf": [{
							                                "title": "Steady state service network",
							                                "properties": {
							                                    "analyticalModel": {
							                                        "type": "object",
							                                        "properties": {
							                                            "name": {
							                                                "type": "string",
							                                                "enum": ["computeMetrics"]
							                                            },
							                                            "ns": {
							                                                "type": "string",
							                                                "enum": ["http://repository.vsnet.gmu.edu/process/mfgSupplyChain/composite/supplyChain/lib/supply_chain.jq"]
							                                            }
							                                        },
							                                        "required": [
							                                            "ns",
							                                            "name"
							                                        ]
							                                    },
							                                    "inputThru": {
							                                        "type": "object",
							                                        "patternProperties": {
							                                            "^[A-Za-z0-9-_/#\\s]+$": {
							                                                "$ref": "#/definitions/decimalVarAnnotatedObj"
							                                            }
							                                        }
							                                    },
							                                    "outputThru": {
							                                        "type": "object",
							                                        "patternProperties": {
							                                            "^[A-Za-z0-9-_/#\\s]+$": {
							                                                "$ref": "#/definitions/decimalVarAnnotatedObj"
							                                            }
							                                        }
							                                    },
							                                    "subProcesses": {
							                                        "type": "array",
							                                        "items": {
							                                            "type": "string",
							                                            "pattern": "^[A-Za-z0-9-_/#\\s]+$"
							                                        }
							                                    }
							                                },
							                                "required": ["analyticalModel", "subProcesses"]
							                            }, {
							                                "title": "Vending service",
							                                "properties": {
							                                    "analyticalModel": {
							                                        "type": "object",
							                                        "properties": {
							                                            "name": {
							                                                "type": "string",
							                                                "enum": ["computeMetrics"]
							                                            },
							                                            "ns": {
							                                                "type": "string",
							                                                "enum": ["http://repository.vsnet.gmu.edu/process/mfgSupplyChain/components/supply/lib/supplier.jq"]
							                                            }
							                                        },
							                                        "required": [
							                                            "ns",
							                                            "name"
							                                        ]
							                                    },
							                                    "outputThru": {
							                                        "type": "object",
							                                        "patternProperties": {
							                                            "^[A-Za-z0-9-_/#\\s]+$": {
							                                                "$ref": "#/definitions/decimalVarAnnotatedObj"
							                                            }
							                                        }
							                                    },
							                                    "ppu": {
							                                        "type": "object",
							                                        "patternProperties": {
							                                            "^[A-Za-z0-9-_/#\\s]+$": {
							                                                "type": "number",
							                                                "minimum": 0
							                                            }
							                                        }
							                                    },
							                                    "co2perUnit": {
							                                        "type": "object",
							                                        "patternProperties": {
							                                            "^[A-Za-z0-9-_/#\\s]+$": {
							                                                "type": "number",
							                                                "minimum": 0
							                                            }
							                                        }
							                                    }
							                                },
							                                "required": ["analyticalModel", "outputThru",
							                                    "ppu", "co2perUnit"
							                                ]
							                            }, {
							                                "title": "Production process",
							                                "properties": {
							                                    "analyticalModel": {
							                                        "type": "object",
							                                        "properties": {
							                                            "name": {
							                                                "type": "string",
							                                                "enum": ["computeMetrics"]
							                                            },
							                                            "ns": {
							                                                "type": "string",
							                                                "enum": ["http://repository.vsnet.gmu.edu/process/mfgSupplyChain/components/simpleMfg/lib/simple_manufacturing.jq"]
							                                            }
							                                        },
							                                        "required": [
							                                            "ns",
							                                            "name"
							                                        ]
							                                    },
							                                    "outputThru": {
							                                        "type": "object",
							                                        "patternProperties": {
							                                            "^[A-Za-z0-9-_/#\\s]+$": {
							                                                "$ref": "#/definitions/decimalVarAnnotatedObj"
							                                            }
							                                        }
							                                    },
							                                    "qtyInPer1out": {
							                                        "type": "object",
							                                        "patternProperties": {
							                                            "^[A-Za-z0-9-_/#\\s]+$": {
							                                                "type": "object",
							                                                "patternProperties": {
							                                                    "^[A-Za-z0-9-_/#\\s]+$": {
							                                                        "type": "integer",
							                                                        "minimum": 0
							                                                    }
							                                                }
							                                            }
							                                        }
							                                    },
							                                    "pwlCoeffs": {
							                                        "type": "object",
							                                        "patternProperties": {
							                                            "^[A-Za-z0-9-_/#\\s]+$": {
							                                                "type": "object",
							                                                "properties": {
							                                                    "bound1": {
							                                                        "type": "number",
							                                                        "minimum": 0
							                                                    },
							                                                    "bound2": {
							                                                        "type": "number",
							                                                        "minimum": 0
							                                                    },
							                                                    "slope1": {
							                                                        "type": "number",
							                                                        "minimum": 0
							                                                    },
							                                                    "slope2": {
							                                                        "type": "number",
							                                                        "minimum": 0
							                                                    },
							                                                    "slope3": {
							                                                        "type": "number",
							                                                        "minimum": 0
							                                                    },
							                                                    "startParam": {
							                                                        "type": "number",
							                                                        "minimum": 0
							                                                    }
							                                                },
							                                                "required": [
							                                                    "startParam",
							                                                    "bound2",
							                                                    "bound1",
							                                                    "slope1",
							                                                    "slope3",
							                                                    "slope2"
							                                                ]
							                                            }
							                                        }
							                                    },
							                                    "co2perUnit": {
							                                        "type": "object",
							                                        "patternProperties": {
							                                            "^[A-Za-z0-9-_/#\\s]+$": {
							                                                "type": "number",
							                                                "minimum": 0
							                                            }
							                                        }
							                                    }
							                                },
							                                "required": [
							                                    "co2perUnit",
							                                    "pwlCoeffs",
							                                    "outputThru",
							                                    "analyticalModel",
							                                    "qtyInPer1out"
							                                ]
							                            }, {
							                                "title": "Contract mfg service",
							                                "properties": {
							                                    "analyticalModel": {
							                                        "type": "object",
							                                        "properties": {
							                                            "name": {
							                                                "type": "string",
							                                                "enum": ["computeMetrics"]
							                                            },
							                                            "ns": {
							                                                "type": "string",
							                                                "enum": ["http://repository.vsnet.gmu.edu/process/mfgSupplyChain/components/contractMfg/lib/contract_manufacturing.jq"]
							                                            }
							                                        },
							                                        "required": [
							                                            "ns",
							                                            "name"
							                                        ]
							                                    },
							                                    "outputThru": {
							                                        "type": "object",
							                                        "patternProperties": {
							                                            "^[A-Za-z0-9-_/#\\s]+$": {
							                                                "$ref": "#/definitions/decimalVarAnnotatedObj"
							                                            }
							                                        }
							                                    },
							                                    "qtyInPer1out": {
							                                        "type": "object",
							                                        "patternProperties": {
							                                            "^[A-Za-z0-9-_/#\\s]+$": {
							                                                "type": "object",
							                                                "patternProperties": {
							                                                    "^[A-Za-z0-9-_/#\\s]+$": {
							                                                        "type": "integer",
							                                                        "minimum": 0
							                                                    }
							                                                }
							                                            }
							                                        }
							                                    },
							                                    "manufCostPerUnit": {
							                                        "type": "object",
							                                        "patternProperties": {
							                                            "^[A-Za-z0-9-_/#\\s]+$": {
							                                                "type": "number",
							                                                "minimum": 0
							                                            }
							                                        }
							                                    },
							                                    "co2perUnit": {
							                                        "type": "object",
							                                        "patternProperties": {
							                                            "^[A-Za-z0-9-_/#\\s]+$": {
							                                                "type": "number",
							                                                "minimum": 0
							                                            }
							                                        }
							                                    }
							                                },
							                                "required": ["analyticalModel", "outputThru", "qtyInPer1out",
							                                    "manufCostPerUnit", "co2perUnit"
							                                ]
							                            }, {
							                                "title": "Demand",
							                                "properties": {
							                                    "analyticalModel": {
							                                        "type": "object",
							                                        "properties": {
							                                            "name": {
							                                                "type": "string",
							                                                "enum": ["computeMetrics"]
							                                            },
							                                            "ns": {
							                                                "type": "string",
							                                                "enum": ["http://repository.vsnet.gmu.edu/process/mfgSupplyChain/components/demand/lib/demand.jq"]
							                                            }
							                                        },
							                                        "required": [
							                                            "ns",
							                                            "name"
							                                        ]
							                                    },
							                                    "inputThru": {
							                                        "type": "object",
							                                        "patternProperties": {
							                                            "^[A-Za-z0-9-_/#\\s]+$": {
							                                                "type": "object",
							                                                "properties": {
							                                                    "v": {
							                                                        "type": "number",
							                                                        "minimum": 0
							                                                    }
							                                                },
							                                                "required": ["v"]
							                                            }
							                                        }
							                                    }
							                                },
							                                "required": ["analyticalModel", "inputThru"]
							                            }, {
							                                "title": "One in one out UMP service",
							                                "properties": {
							                                    "analyticalModel": {
							                                        "type": "object",
							                                        "properties": {
							                                            "name": {
							                                                "type": "string",
							                                                "enum": ["computeMetrics"]
							                                            },
							                                            "ns": {
							                                                "type": "string",
							                                                "enum": ["http://repository.vsnet.gmu.edu/process/mfgSupplyChain/umpService/oneInOneOutUMPService/lib/one_in_one_out_ump_service.jq"]
							                                            }
							                                        },
							                                        "required": [
							                                            "ns",
							                                            "name"
							                                        ]
							                                    },
							                                    "inputItem": {
							                                        "type": "string",
							                                        "pattern": "^[A-Za-z0-9-_/#\\s]+$"
							                                    },
							                                    "outputItem": {
							                                        "type": "string",
							                                        "pattern": "^[A-Za-z0-9-_/#\\s]+$"
							                                    },
							                                    "umpInput": {
							                                        "type": "object",
							                                        "oneOf": [{
							                                            "title": "Machining UPLCI",
							                                            "properties": {
							                                                "analyticalModel": {
							                                                    "type": "object",
							                                                    "properties": {
							                                                        "name": {
							                                                            "type": "string",
							                                                            "enum": ["computeMetrics"]
							                                                        },
							                                                        "ns": {
							                                                            "type": "string",
							                                                            "enum": ["http://repository.vsnet.gmu.edu/process/umpMfgProcess/shapingProcess/subtractionProcess/machiningSequence/lib/machining.jq",
							                                                                "http://repository.vsnet.gmu.edu/process/umpMfgProcess/shapingProcess/subtractionProcess/mechanicalSubtraction/multiPointCutting/heatSinkPart/lib/heatSinkMachine.jq"
							                                                            ]
							                                                        }
							                                                    },
							                                                    "required": [
							                                                        "ns",
							                                                        "name"
							                                                    ]
							                                                },
							                                                "machingSteps": {
							                                                    "type": "array",
							                                                    "items": {
							                                                        "type": "string",
							                                                        "pattern": "^[A-Za-z0-9-_/#\\s]+$"
							                                                    }
							                                                },
							                                                "stepsInputs": {
							                                                    "type": "object",
							                                                    "patternProperties": {
							                                                        "^[A-Za-z0-9-_/#\\s]+$": {
							                                                            "type": "object",
							                                                            "oneOf": [{
							                                                                    "title": "Milling UPLCI",
							                                                                    "properties": {
							                                                                        "analyticalModel": {
							                                                                            "type": "object",
							                                                                            "properties": {
							                                                                                "name": {
							                                                                                    "type": "string",
							                                                                                    "enum": ["computeMetrics"]
							                                                                                },
							                                                                                "ns": {
							                                                                                    "type": "string",
							                                                                                    "enum": ["http://repository.vsnet.gmu.edu/process/umpMfgProcess/shapingProcess/subtractionProcess/mechanicalSubtraction/multiPointCutting/milling/horizontalVerticalMilling/lib/millingMachine.jq"]
							                                                                                }
							                                                                            },
							                                                                            "required": [
							                                                                                "name",
							                                                                                "ns"
							                                                                            ]
							                                                                        },
							                                                                        "CO2_per_kwh": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "D": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "Material": {
							                                                                            "type": "object",
							                                                                            "properties": {
							                                                                                "dimentions": {
							                                                                                    "type": "object",
							                                                                                    "properties": {
							                                                                                        "H": {
							                                                                                            "type": "number",
							                                                                                            "minimum": 0,
							                                                                                            "exclusiveMinimum": true
							                                                                                        },
							                                                                                        "L": {
							                                                                                            "type": "number",
							                                                                                            "minimum": 0,
							                                                                                            "exclusiveMinimum": true
							                                                                                        },
							                                                                                        "W": {
							                                                                                            "type": "number",
							                                                                                            "minimum": 0,
							                                                                                            "exclusiveMinimum": true
							                                                                                        }
							                                                                                    },
							                                                                                    "required": [
							                                                                                        "H",
							                                                                                        "L",
							                                                                                        "W"
							                                                                                    ]
							                                                                                },
							                                                                                "type": {
							                                                                                    "type": "string"
							                                                                                }
							                                                                            },
							                                                                            "required": [
							                                                                                "type",
							                                                                                "dimentions"
							                                                                            ]
							                                                                        },
							                                                                        "V": {
							                                                                            "$ref": "#/definitions/decimalVarAnnotatedObj"
							                                                                        },
							                                                                        "centered": {
							                                                                            "type": "string",
							                                                                            "enum": ["yes", "no"]
							                                                                        },
							                                                                        "depth": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "distance_approach": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "distance_offset": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "distance_overtravel": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "energyCost_per_kwh": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "f_t": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "machineCost": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "millType": {
							                                                                            "type": "string",
							                                                                            "enum": ["peripheral", "face"]
							                                                                        },
							                                                                        "n_t": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "p_axis": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "p_basic": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "p_coolant": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "p_spindle": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "t_cleaning": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "t_loading": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "t_retract": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "t_unloading": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "traverse_h": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "traverse_v": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        }
							                                                                    },
							                                                                    "required": [
							                                                                        "analyticalModel",
							                                                                        "distance_approach",
							                                                                        "t_retract",
							                                                                        "n_t",
							                                                                        "t_cleaning",
							                                                                        "distance_overtravel",
							                                                                        "t_loading",
							                                                                        "p_spindle",
							                                                                        "Material",
							                                                                        "p_axis",
							                                                                        "D",
							                                                                        "centered",
							                                                                        "t_unloading",
							                                                                        "CO2_per_kwh",
							                                                                        "machineCost",
							                                                                        "energyCost_per_kwh",
							                                                                        "V",
							                                                                        "p_coolant",
							                                                                        "p_basic",
							                                                                        "traverse_h",
							                                                                        "distance_offset",
							                                                                        "f_t",
							                                                                        "depth",
							                                                                        "millType",
							                                                                        "traverse_v"
							                                                                    ]
							                                                                },
							                                                                {
							                                                                    "title": "Drilling UPLCI",
							                                                                    "properties": {
							                                                                        "analyticalModel": {
							                                                                            "type": "object",
							                                                                            "properties": {
							                                                                                "name": {
							                                                                                    "type": "string",
							                                                                                    "enum": ["computeMetrics"]
							                                                                                },
							                                                                                "ns": {
							                                                                                    "type": "string",
							                                                                                    "enum": ["http://repository.vsnet.gmu.edu/process/umpMfgProcess/shapingProcess/subtractionProcess/mechanicalSubtraction/multiPointCutting/holeMaking/drilling/lib/drillingMachine.jq"]
							                                                                                }
							                                                                            },
							                                                                            "required": [
							                                                                                "name",
							                                                                                "ns"
							                                                                            ]
							                                                                        },
							                                                                        "CO2_per_kwh": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "D": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "Holes": {
							                                                                            "type": "object",
							                                                                            "oneOf": [{
							                                                                                "title": "Non zero holes",
							                                                                                "properties": {
							                                                                                    "holesApart": {
							                                                                                        "type": "number",
							                                                                                        "minimum": 0,
							                                                                                        "exclusiveMinimum": true
							                                                                                    },
							                                                                                    "noOfHoles": {
							                                                                                        "type": "integer",
							                                                                                        "minimum": 0
							                                                                                    }
							                                                                                },
							                                                                                "required": [
							                                                                                    "noOfHoles",
							                                                                                    "holesApart"
							                                                                                ]
							                                                                            }, {
							                                                                                "title": "Zero holes",
							                                                                                "properties": {
							                                                                                    "noOfHoles": {
							                                                                                        "type": "integer",
							                                                                                        "enum": [0]
							                                                                                    }
							                                                                                },
							                                                                                "required": ["noOfHoles"],
							                                                                                "not": {
							                                                                                    "required": ["holesApart"]
							                                                                                }
							                                                                            }]
							                                                                        },
							                                                                        "Material": {
							                                                                            "type": "object",
							                                                                            "properties": {
							                                                                                "dimentions": {
							                                                                                    "type": "object",
							                                                                                    "properties": {
							                                                                                        "H": {
							                                                                                            "type": "number",
							                                                                                            "minimum": 0,
							                                                                                            "exclusiveMinimum": true
							                                                                                        },
							                                                                                        "L": {
							                                                                                            "type": "number",
							                                                                                            "minimum": 0,
							                                                                                            "exclusiveMinimum": true
							                                                                                        },
							                                                                                        "W": {
							                                                                                            "type": "number",
							                                                                                            "minimum": 0,
							                                                                                            "exclusiveMinimum": true
							                                                                                        }
							                                                                                    },
							                                                                                    "required": [
							                                                                                        "H",
							                                                                                        "L",
							                                                                                        "W"
							                                                                                    ]
							                                                                                },
							                                                                                "type": {
							                                                                                    "type": "string"
							                                                                                }
							                                                                            },
							                                                                            "required": [
							                                                                                "type",
							                                                                                "dimentions"
							                                                                            ]
							                                                                        },
							                                                                        "V": {
							                                                                            "$ref": "#/definitions/decimalVarAnnotatedObj"
							                                                                        },
							                                                                        "energyCost_per_kwh": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "machineCost": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "p_axis": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "p_basic": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "p_coolant": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "p_spindle": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "t_cleaning": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "t_loading": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "t_unloading": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "traverse_h": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "traverse_v": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        },
							                                                                        "weight": {
							                                                                            "type": "number",
							                                                                            "minimum": 0,
							                                                                            "exclusiveMinimum": true
							                                                                        }
							                                                                    },
							                                                                    "required": [
							                                                                        "analyticalModel",
							                                                                        "t_loading",
							                                                                        "traverse_h",
							                                                                        "p_axis",
							                                                                        "D",
							                                                                        "weight",
							                                                                        "Material",
							                                                                        "Holes",
							                                                                        "p_spindle",
							                                                                        "t_unloading",
							                                                                        "t_cleaning",
							                                                                        "p_basic",
							                                                                        "machineCost",
							                                                                        "CO2_per_kwh",
							                                                                        "energyCost_per_kwh",
							                                                                        "V",
							                                                                        "p_coolant",
							                                                                        "traverse_v"
							                                                                    ]
							                                                                }
							                                                            ]
							                                                        }
							                                                    }
							                                                }
							                                            },
							                                            "required": [
							                                                "stepsInputs",
							                                                "machingSteps",
							                                                "analyticalModel"
							                                            ]
							                                        }, {
							                                            "title": "Shearing UPLCI",
							                                            "properties": {
							                                                "analyticalModel": {
							                                                    "type": "object",
							                                                    "properties": {
							                                                        "name": {
							                                                            "type": "string",
							                                                            "enum": ["computeMetrics"]
							                                                        },
							                                                        "ns": {
							                                                            "type": "string",
							                                                            "enum": ["http://repository.vsnet.gmu.edu/process/umpMfgProcess/shapingProcess/subtractionProcess/shearing/lib/shearingMachine.jq"]
							                                                        }
							                                                    },
							                                                    "required": [
							                                                        "name",
							                                                        "ns"
							                                                    ]
							                                                },
							                                                "CO2_per_kwh": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "D": {
							                                                    "type": "number",
							                                                    "minimum": 0
							                                                },
							                                                "F": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "L": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "Material": {
							                                                    "type": "object",
							                                                    "properties": {
							                                                        "type": {
							                                                            "type": "string"
							                                                        }
							                                                    },
							                                                    "required": ["type"]
							                                                },
							                                                "P_basic": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "P_idle": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "R": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "T": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "V": {
							                                                    "$ref": "#/definitions/decimalVarAnnotatedObj"
							                                                },
							                                                "VTR": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "W": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "energyCost_per_kwh": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "machineCost": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "t_handling": {
							                                                    "type": "object",
							                                                    "properties": {
							                                                        "lb": {
							                                                            "type": "number",
							                                                            "minimum": 0,
							                                                            "exclusiveMinimum": true
							                                                        },
							                                                        "ub": {
							                                                            "type": "number",
							                                                            "minimum": 0,
							                                                            "exclusiveMinimum": true
							                                                        },
							                                                        "unit": {
							                                                            "type": "string"
							                                                        }
							                                                    },
							                                                    "required": ["unit"]
							                                                },
							                                                "toolType": {
							                                                    "type": "string",
							                                                    "enum": ["straight", "non-straight", "circular"]
							                                                }
							                                            },
							                                            "required": [
							                                                "analyticalModel",
							                                                "P_basic",
							                                                "D",
							                                                "toolType",
							                                                "F",
							                                                "Material",
							                                                "L",
							                                                "machineCost",
							                                                "CO2_per_kwh",
							                                                "R",
							                                                "T",
							                                                "W",
							                                                "V",
							                                                "VTR",
							                                                "energyCost_per_kwh",
							                                                "P_idle"
							                                            ]
							                                        }, {
							                                            "title": "Drilling UPLCI",
							                                            "properties": {
							                                                "analyticalModel": {
							                                                    "type": "object",
							                                                    "properties": {
							                                                        "name": {
							                                                            "type": "string",
							                                                            "enum": ["computeMetrics"]
							                                                        },
							                                                        "ns": {
							                                                            "type": "string",
							                                                            "enum": ["http://repository.vsnet.gmu.edu/process/umpMfgProcess/shapingProcess/subtractionProcess/mechanicalSubtraction/multiPointCutting/holeMaking/drilling/lib/drillingMachine.jq"]
							                                                        }
							                                                    },
							                                                    "required": [
							                                                        "ns",
							                                                        "name"
							                                                    ]
							                                                },
							                                                "CO2_per_kwh": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "D": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "Holes": {
							                                                    "type": "object",
							                                                    "oneOf": [{
							                                                        "title": "Non zero holes",
							                                                        "properties": {
							                                                            "holesApart": {
							                                                                "type": "number",
							                                                                "minimum": 0,
							                                                                "exclusiveMinimum": true
							                                                            },
							                                                            "noOfHoles": {
							                                                                "type": "integer",
							                                                                "minimum": 0
							                                                            }
							                                                        },
							                                                        "required": [
							                                                            "noOfHoles",
							                                                            "holesApart"
							                                                        ]
							                                                    }, {
							                                                        "title": "Zero holes",
							                                                        "properties": {
							                                                            "noOfHoles": {
							                                                                "type": "integer",
							                                                                "enum": [0]
							                                                            }
							                                                        },
							                                                        "required": ["noOfHoles"],
							                                                        "not": {
							                                                            "required": ["holesApart"]
							                                                        }
							                                                    }]
							                                                },
							                                                "Material": {
							                                                    "type": "object",
							                                                    "properties": {
							                                                        "dimentions": {
							                                                            "type": "object",
							                                                            "properties": {
							                                                                "H": {
							                                                                    "type": "number",
							                                                                    "minimum": 0,
							                                                                    "exclusiveMinimum": true
							                                                                },
							                                                                "L": {
							                                                                    "type": "number",
							                                                                    "minimum": 0,
							                                                                    "exclusiveMinimum": true
							                                                                },
							                                                                "W": {
							                                                                    "type": "number",
							                                                                    "minimum": 0,
							                                                                    "exclusiveMinimum": true
							                                                                }
							                                                            },
							                                                            "required": [
							                                                                "H",
							                                                                "L",
							                                                                "W"
							                                                            ]
							                                                        },
							                                                        "type": {
							                                                            "type": "string"
							                                                        }
							                                                    },
							                                                    "required": [
							                                                        "type",
							                                                        "dimentions"
							                                                    ]
							                                                },
							                                                "V": {
							                                                    "$ref": "#/definitions/decimalVarAnnotatedObj"
							                                                },
							                                                "energyCost_per_kwh": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "machineCost": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "p_axis": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "p_basic": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "p_coolant": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "p_spindle": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "t_cleaning": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "t_loading": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "t_unloading": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "traverse_h": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "traverse_v": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "weight": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                }
							                                            },
							                                            "required": [
							                                                "analyticalModel",
							                                                "t_loading",
							                                                "traverse_h",
							                                                "p_axis",
							                                                "D",
							                                                "weight",
							                                                "Material",
							                                                "Holes",
							                                                "p_spindle",
							                                                "t_unloading",
							                                                "t_cleaning",
							                                                "p_basic",
							                                                "machineCost",
							                                                "CO2_per_kwh",
							                                                "energyCost_per_kwh",
							                                                "V",
							                                                "p_coolant",
							                                                "traverse_v"
							                                            ]
							                                        }, {
							                                            "title": "Milling UPLCI",
							                                            "properties": {
							                                                "analyticalModel": {
							                                                    "type": "object",
							                                                    "properties": {
							                                                        "name": {
							                                                            "type": "string",
							                                                            "enum": ["computeMetrics"]
							                                                        },
							                                                        "ns": {
							                                                            "type": "string",
							                                                            "enum": ["http://repository.vsnet.gmu.edu/process/umpMfgProcess/shapingProcess/subtractionProcess/mechanicalSubtraction/multiPointCutting/milling/horizontalVerticalMilling/lib/millingMachine.jq"]
							                                                        }
							                                                    },
							                                                    "required": [
							                                                        "name",
							                                                        "ns"
							                                                    ]
							                                                },
							                                                "CO2_per_kwh": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "D": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "Material": {
							                                                    "type": "object",
							                                                    "properties": {
							                                                        "dimentions": {
							                                                            "type": "object",
							                                                            "properties": {
							                                                                "H": {
							                                                                    "type": "number",
							                                                                    "minimum": 0,
							                                                                    "exclusiveMinimum": true
							                                                                },
							                                                                "L": {
							                                                                    "type": "number",
							                                                                    "minimum": 0,
							                                                                    "exclusiveMinimum": true
							                                                                },
							                                                                "W": {
							                                                                    "type": "number",
							                                                                    "minimum": 0,
							                                                                    "exclusiveMinimum": true
							                                                                }
							                                                            },
							                                                            "required": [
							                                                                "H",
							                                                                "L",
							                                                                "W"
							                                                            ]
							                                                        },
							                                                        "type": {
							                                                            "type": "string"
							                                                        }
							                                                    },
							                                                    "required": [
							                                                        "type",
							                                                        "dimentions"
							                                                    ]
							                                                },
							                                                "V": {
							                                                    "$ref": "#/definitions/decimalVarAnnotatedObj"
							                                                },
							                                                "centered": {
							                                                    "type": "string",
							                                                    "enum": ["yes", "no"]
							                                                },
							                                                "depth": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "distance_approach": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "distance_offset": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "distance_overtravel": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "energyCost_per_kwh": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "f_t": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "machineCost": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "millType": {
							                                                    "type": "string",
							                                                    "enum": ["peripheral", "face"]
							                                                },
							                                                "n_t": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "p_axis": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "p_basic": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "p_coolant": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "p_spindle": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "t_cleaning": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "t_loading": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "t_retract": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "t_unloading": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "traverse_h": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "traverse_v": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                }
							                                            },
							                                            "required": [
							                                                "analyticalModel",
							                                                "distance_approach",
							                                                "t_retract",
							                                                "n_t",
							                                                "t_cleaning",
							                                                "distance_overtravel",
							                                                "t_loading",
							                                                "p_spindle",
							                                                "Material",
							                                                "p_axis",
							                                                "D",
							                                                "centered",
							                                                "t_unloading",
							                                                "CO2_per_kwh",
							                                                "machineCost",
							                                                "energyCost_per_kwh",
							                                                "V",
							                                                "p_coolant",
							                                                "p_basic",
							                                                "traverse_h",
							                                                "distance_offset",
							                                                "f_t",
							                                                "depth",
							                                                "millType",
							                                                "traverse_v"
							                                            ]
							                                        }, {
							                                            "title": "Smelting Dorward",
							                                            "properties": {
							                                                "analyticalModel": {
							                                                    "type": "object",
							                                                    "properties": {
							                                                        "name": {
							                                                            "type": "string",
							                                                            "enum": ["computeMetrics"]
							                                                        },
							                                                        "ns": {
							                                                            "type": "string",
							                                                            "enum": ["http://repository.vsnet.gmu.edu/process/umpMfgProcess/shapingProcess/consolidationProcess/chemicalThermalJoining/smelting/lib/smelting.jq"]
							                                                        }
							                                                    },
							                                                    "required": [
							                                                        "ns",
							                                                        "name"
							                                                    ]
							                                                },
							                                                "A": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "ACD": {
							                                                    "$ref": "#/definitions/decimalVarAnnotatedObj"
							                                                },
							                                                "CO2_per_kwh": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "energyCost_per_kwh": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "i_d": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "rho": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "v_d": {
							                                                    "type": "number",
							                                                    "minimum": 0,
							                                                    "exclusiveMinimum": true
							                                                },
							                                                "v_i": {
							                                                    "type": "array",
							                                                    "items": {
							                                                        "type": "number",
							                                                        "minimum": 0
							                                                    }
							                                                }
							                                            },
							                                            "required": [
							                                                "A",
							                                                "CO2_per_kwh",
							                                                "energyCost_per_kwh",
							                                                "ACD",
							                                                "i_d",
							                                                "v_d",
							                                                "rho",
							                                                "v_i",
							                                                "analyticalModel"
							                                            ]
							                                        }]
							                                    }
							                                },
							                                "required": [
							                                    "analyticalModel",
							                                    "inputItem",
							                                    "outputItem",
							                                    "umpInput"
							                                ]
							                            }]
							                        }
							                    }
							                }
							            },
							            "required": [
							                "root",
							                "kb"
							            ]
							        }
							    },
							    "required": ["input", "config"]
							}

							    

						});
				form.setValue(JSON.parse(data));
				form.on('change',function() {
					$("#new-content1").val(JSON.stringify(form.getValue(), null, 4));
				});
			} catch (e) {
			}
			
			//alert(data);
			/*
				$("#results").text(data);
				var editor123 = ace.edit("results");
				editor123.setOptions({
					maxLines : Infinity,
					showPrintMargin : false
				});
				editor123.setTheme("ace/theme/eclipse");
				var modelist = ace.require("ace/ext/modelist");
				var mode = modelist.getModeForPath("test.json").mode;
				editor123.session.setMode(mode);
			*/
			
			
			
			
		},
		error : function(data) {
			$("#results").text(data);
		}
	});
</script>
