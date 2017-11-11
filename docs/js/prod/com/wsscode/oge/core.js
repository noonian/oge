// Compiled by ClojureScript 1.9.946 {:static-fns true, :optimize-constants true}
goog.provide('com.wsscode.oge.core');
goog.require('cljs.core');
goog.require('cljs.core.constants');
goog.require('fulcro.client.core');
goog.require('fulcro.client.data_fetch');
goog.require('fulcro.client.mutations');
goog.require('fulcro_css.css');
goog.require('com.wsscode.pathom.core');
goog.require('com.wsscode.pathom.profile');
goog.require('com.wsscode.pathom.connect');
goog.require('com.wsscode.oge.ui.codemirror');
goog.require('com.wsscode.oge.ui.flame_graph');
goog.require('com.wsscode.oge.ui.helpers');
goog.require('om.next');
goog.require('om.dom');
goog.require('cljs.pprint');
goog.require('cljs.reader');
goog.require('com.wsscode.oge.ui.common');
fulcro.client.mutations.mutate.cljs$core$IMultiFn$_add_method$arity$3(null,cljs.core.cst$sym$com$wsscode$oge$core_SLASH_clear_DASH_errors,(function (p__42548,_,___$1){
var map__42549 = p__42548;
var map__42549__$1 = ((((!((map__42549 == null)))?((((map__42549.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42549.cljs$core$ISeq$)))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42549):map__42549);
var state = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42549__$1,cljs.core.cst$kw$state);
return new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$action,((function (map__42549,map__42549__$1,state){
return (function (){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(state,cljs.core.dissoc,cljs.core.cst$kw$com$wsscode$pathom$core_SLASH_errors);
});})(map__42549,map__42549__$1,state))
], null);
}));
fulcro.client.mutations.mutate.cljs$core$IMultiFn$_add_method$arity$3(null,cljs.core.cst$sym$com$wsscode$oge$core_SLASH_normalize_DASH_result,(function (p__42551,_,___$1){
var map__42552 = p__42551;
var map__42552__$1 = ((((!((map__42552 == null)))?((((map__42552.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42552.cljs$core$ISeq$)))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42552):map__42552);
var ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42552__$1,cljs.core.cst$kw$ref);
var state = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42552__$1,cljs.core.cst$kw$state);
return new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$action,((function (map__42552,map__42552__$1,ref,state){
return (function (){
var result_SINGLEQUOTE_ = (function (){var G__42554 = cljs.core.cst$kw$oge_SLASH_result_SINGLEQUOTE_.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(state),ref));
if(cljs.core.truth_(cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(state),cljs.core.cst$kw$com$wsscode$pathom$core_SLASH_errors))){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__42554,cljs.core.cst$kw$com$wsscode$pathom$core_SLASH_errors,cljs.core.into.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentArrayMap.EMPTY,cljs.core.map.cljs$core$IFn$_invoke$arity$1(((function (G__42554,map__42552,map__42552__$1,ref,state){
return (function (p__42555){
var vec__42556 = p__42555;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42556,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42556,(1),null);
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.vec(cljs.core.next(k)),v], null);
});})(G__42554,map__42552,map__42552__$1,ref,state))
),cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(state),cljs.core.cst$kw$com$wsscode$pathom$core_SLASH_errors)));
} else {
return G__42554;
}
})();
var profile = (function (){var G__42559 = result_SINGLEQUOTE_;
var G__42559__$1 = (((G__42559 == null))?null:cljs.core.cst$kw$com$wsscode$pathom$profile_SLASH_profile.cljs$core$IFn$_invoke$arity$1(G__42559));
if((G__42559__$1 == null)){
return null;
} else {
return cljs.core.cst$kw$_GT__SLASH_oge.cljs$core$IFn$_invoke$arity$1(G__42559__$1);
}
})();
var result = (function (){var sb__11539__auto__ = (new goog.string.StringBuffer());
var _STAR_print_newline_STAR_42560_42562 = cljs.core._STAR_print_newline_STAR_;
var _STAR_print_fn_STAR_42561_42563 = cljs.core._STAR_print_fn_STAR_;
cljs.core._STAR_print_newline_STAR_ = true;

cljs.core._STAR_print_fn_STAR_ = ((function (_STAR_print_newline_STAR_42560_42562,_STAR_print_fn_STAR_42561_42563,sb__11539__auto__,result_SINGLEQUOTE_,profile,map__42552,map__42552__$1,ref,state){
return (function (x__11540__auto__){
return sb__11539__auto__.append(x__11540__auto__);
});})(_STAR_print_newline_STAR_42560_42562,_STAR_print_fn_STAR_42561_42563,sb__11539__auto__,result_SINGLEQUOTE_,profile,map__42552,map__42552__$1,ref,state))
;

try{cljs.pprint.pprint.cljs$core$IFn$_invoke$arity$1(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(result_SINGLEQUOTE_,cljs.core.cst$kw$com$wsscode$pathom$profile_SLASH_profile));
}finally {cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR_42561_42563;

cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR_42560_42562;
}
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(sb__11539__auto__)].join('');
})();
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(state,cljs.core.update_in,ref,cljs.core.merge,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$oge_SLASH_result,result,cljs.core.cst$kw$oge_SLASH_profile,profile], null)], 0));
});})(map__42552,map__42552__$1,ref,state))
], null);
}));
com.wsscode.oge.core.oge_query = (function com$wsscode$oge$core$oge_query(this$,query){
try{return om.next.transact_BANG_.cljs$core$IFn$_invoke$arity$2(this$,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$1(cljs.core._conj(cljs.core.List.EMPTY,cljs.core.cst$sym$com$wsscode$oge$core_SLASH_clear_DASH_errors)))),cljs.core._conj((function (){var x__11366__auto__ = new cljs.core.PersistentArrayMap(null, 4, [cljs.core.cst$kw$target,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(om.next.get_ident(this$),cljs.core.cst$kw$oge_SLASH_result_SINGLEQUOTE_),cljs.core.cst$kw$query,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$_GT__SLASH_oge,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.reader.read_string.cljs$core$IFn$_invoke$arity$1(query),cljs.core.cst$kw$com$wsscode$pathom$profile_SLASH_profile)], null)], null),cljs.core.cst$kw$refresh,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$oge_SLASH_result], null),cljs.core.cst$kw$post_DASH_mutation,cljs.core.cst$sym$com$wsscode$oge$core_SLASH_normalize_DASH_result], null);
return cljs.core._conj(cljs.core.List.EMPTY,x__11366__auto__);
})(),cljs.core.cst$sym$fulcro_SLASH_load)], null));
}catch (e42564){var e = e42564;
return console.error("Invalid query",e);
}});
if(typeof com.wsscode.oge.core.Oge !== 'undefined'){
} else {
/**
 * @constructor
 */
com.wsscode.oge.core.Oge = (function com$wsscode$oge$core$Oge(){
var this__20292__auto__ = this;
React.Component.apply(this__20292__auto__,arguments);

if(!((this__20292__auto__.initLocalState == null))){
this__20292__auto__.state = this__20292__auto__.initLocalState();
} else {
this__20292__auto__.state = {};
}

return this__20292__auto__;
});

com.wsscode.oge.core.Oge.prototype = goog.object.clone(React.Component.prototype);
}

var x42568_42591 = com.wsscode.oge.core.Oge.prototype;
x42568_42591.componentWillUpdate = ((function (x42568_42591){
return (function (next_props__20043__auto__,next_state__20044__auto__){
var this__20042__auto__ = this;
if(((!((this__20042__auto__ == null)))?(((false) || ((cljs.core.PROTOCOL_SENTINEL === this__20042__auto__.om$next$Ident$)))?true:false):false)){
var ident__20046__auto___42592 = om.next.ident(this__20042__auto__,om.next.props(this__20042__auto__));
var next_ident__20047__auto___42593 = om.next.ident(this__20042__auto__,om.next._next_props(next_props__20043__auto__,this__20042__auto__));
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(ident__20046__auto___42592,next_ident__20047__auto___42593)){
var idxr__20048__auto___42594 = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(om.next.get_reconciler(this__20042__auto__),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$config,cljs.core.cst$kw$indexer], null));
if((idxr__20048__auto___42594 == null)){
} else {
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(cljs.core.cst$kw$indexes.cljs$core$IFn$_invoke$arity$1(idxr__20048__auto___42594),((function (idxr__20048__auto___42594,ident__20046__auto___42592,next_ident__20047__auto___42593,this__20042__auto__,x42568_42591){
return (function (indexes__20049__auto__){
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(indexes__20049__auto__,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$ref_DASH__GT_components,ident__20046__auto___42592], null),cljs.core.disj,this__20042__auto__),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$ref_DASH__GT_components,next_ident__20047__auto___42593], null),cljs.core.fnil.cljs$core$IFn$_invoke$arity$2(cljs.core.conj,cljs.core.PersistentHashSet.EMPTY),this__20042__auto__);
});})(idxr__20048__auto___42594,ident__20046__auto___42592,next_ident__20047__auto___42593,this__20042__auto__,x42568_42591))
);
}
} else {
}
} else {
}

om.next.merge_pending_props_BANG_(this__20042__auto__);

return om.next.merge_pending_state_BANG_(this__20042__auto__);
});})(x42568_42591))
;

x42568_42591.shouldComponentUpdate = ((function (x42568_42591){
return (function (next_props__20043__auto__,next_state__20044__auto__){
var this__20042__auto__ = this;
var next_children__20045__auto__ = next_props__20043__auto__.children;
var next_props__20043__auto____$1 = goog.object.get(next_props__20043__auto__,"omcljs$value");
var next_props__20043__auto____$2 = (function (){var G__42570 = next_props__20043__auto____$1;
if((next_props__20043__auto____$1 instanceof om.next.OmProps)){
return om.next.unwrap(G__42570);
} else {
return G__42570;
}
})();
var or__10412__auto__ = cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(om.next.props(this__20042__auto__),next_props__20043__auto____$2);
if(or__10412__auto__){
return or__10412__auto__;
} else {
var or__10412__auto____$1 = (function (){var and__10400__auto__ = this__20042__auto__.state;
if(cljs.core.truth_(and__10400__auto__)){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2((function (){var G__42575 = this__20042__auto__.state;
var G__42576 = "omcljs$state";
return goog.object.get(G__42575,G__42576);
})(),goog.object.get(next_state__20044__auto__,"omcljs$state"));
} else {
return and__10400__auto__;
}
})();
if(cljs.core.truth_(or__10412__auto____$1)){
return or__10412__auto____$1;
} else {
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(this__20042__auto__.props.children,next_children__20045__auto__);
}
}
});})(x42568_42591))
;

x42568_42591.componentWillUnmount = ((function (x42568_42591){
return (function (){
var this__20042__auto__ = this;
var r__20053__auto__ = om.next.get_reconciler(this__20042__auto__);
var cfg__20054__auto__ = cljs.core.cst$kw$config.cljs$core$IFn$_invoke$arity$1(r__20053__auto__);
var st__20055__auto__ = cljs.core.cst$kw$state.cljs$core$IFn$_invoke$arity$1(cfg__20054__auto__);
var indexer__20052__auto__ = cljs.core.cst$kw$indexer.cljs$core$IFn$_invoke$arity$1(cfg__20054__auto__);
if(cljs.core.truth_((function (){var and__10400__auto__ = !((st__20055__auto__ == null));
if(and__10400__auto__){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(st__20055__auto__),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$om$next_SLASH_queries,this__20042__auto__], null));
} else {
return and__10400__auto__;
}
})())){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(st__20055__auto__,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$om$next_SLASH_queries], null),cljs.core.dissoc,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([this__20042__auto__], 0));
} else {
}

if((indexer__20052__auto__ == null)){
return null;
} else {
return om.next.protocols.drop_component_BANG_(indexer__20052__auto__,this__20042__auto__);
}
});})(x42568_42591))
;

x42568_42591.componentDidUpdate = ((function (x42568_42591){
return (function (prev_props__20050__auto__,prev_state__20051__auto__){
var this__20042__auto__ = this;
return om.next.clear_prev_props_BANG_(this__20042__auto__);
});})(x42568_42591))
;

x42568_42591.isMounted = ((function (x42568_42591){
return (function (){
var this__20042__auto__ = this;
return cljs.core.boolean$(goog.object.getValueByKeys(this__20042__auto__,"_reactInternalInstance","_renderedComponent"));
});})(x42568_42591))
;

x42568_42591.componentWillMount = ((function (x42568_42591){
return (function (){
var this__20042__auto__ = this;
var indexer__20052__auto__ = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(om.next.get_reconciler(this__20042__auto__),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$config,cljs.core.cst$kw$indexer], null));
if((indexer__20052__auto__ == null)){
return null;
} else {
return om.next.protocols.index_component_BANG_(indexer__20052__auto__,this__20042__auto__);
}
});})(x42568_42591))
;

x42568_42591.render = ((function (x42568_42591){
return (function (){
var this__20041__auto__ = this;
var this$ = this__20041__auto__;
var _STAR_reconciler_STAR_42577 = om.next._STAR_reconciler_STAR_;
var _STAR_depth_STAR_42578 = om.next._STAR_depth_STAR_;
var _STAR_shared_STAR_42579 = om.next._STAR_shared_STAR_;
var _STAR_instrument_STAR_42580 = om.next._STAR_instrument_STAR_;
var _STAR_parent_STAR_42581 = om.next._STAR_parent_STAR_;
om.next._STAR_reconciler_STAR_ = om.next.get_reconciler(this__20041__auto__);

om.next._STAR_depth_STAR_ = (om.next.depth(this__20041__auto__) + (1));

om.next._STAR_shared_STAR_ = om.next.shared.cljs$core$IFn$_invoke$arity$1(this__20041__auto__);

om.next._STAR_instrument_STAR_ = om.next.instrument(this__20041__auto__);

om.next._STAR_parent_STAR_ = this__20041__auto__;

try{var map__42582 = om.next.props(this$);
var map__42582__$1 = ((((!((map__42582 == null)))?((((map__42582.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42582.cljs$core$ISeq$)))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42582):map__42582);
var query = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42582__$1,cljs.core.cst$kw$oge_SLASH_query);
var result = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42582__$1,cljs.core.cst$kw$oge_SLASH_result);
var profile = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42582__$1,cljs.core.cst$kw$oge_SLASH_profile);
var result_SINGLEQUOTE_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42582__$1,cljs.core.cst$kw$oge_SLASH_result_SINGLEQUOTE_);
var indexes = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42582__$1,cljs.core.cst$kw$com$wsscode$pathom$connect_SLASH_indexes);
var map__42583 = om.next.get_computed.cljs$core$IFn$_invoke$arity$1(om.next.props(this$));
var map__42583__$1 = ((((!((map__42583 == null)))?((((map__42583.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42583.cljs$core$ISeq$)))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42583):map__42583);
var style = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42583__$1,cljs.core.cst$kw$style);
var css = fulcro_css.css.get_classnames(com.wsscode.oge.core.Oge);
return om.dom.div.cljs$core$IFn$_invoke$arity$variadic(({"className": [cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.cst$kw$container.cljs$core$IFn$_invoke$arity$1(css))," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(((cljs.core.not(profile))?cljs.core.cst$kw$simple.cljs$core$IFn$_invoke$arity$1(css):null))].join(''), "style": cljs.core.clj__GT_js(style)}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([om.dom.div.cljs$core$IFn$_invoke$arity$variadic(({"className": cljs.core.cst$kw$title.cljs$core$IFn$_invoke$arity$1(css)}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([om.dom.h2.cljs$core$IFn$_invoke$arity$variadic(({"className": ["flex ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.cst$kw$title_DASH_oge.cljs$core$IFn$_invoke$arity$1(css))].join('')}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["OgE"], 0)),(function (){var G__42586 = new cljs.core.PersistentArrayMap(null, 3, [cljs.core.cst$kw$style,({"marginLeft": (10)}),cljs.core.cst$kw$disabled,fulcro.client.data_fetch.loading_QMARK_(cljs.core.cst$kw$ui_SLASH_fetch_DASH_state.cljs$core$IFn$_invoke$arity$1(result_SINGLEQUOTE_)),cljs.core.cst$kw$onClick,((function (map__42582,map__42582__$1,query,result,profile,result_SINGLEQUOTE_,indexes,map__42583,map__42583__$1,style,css,_STAR_reconciler_STAR_42577,_STAR_depth_STAR_42578,_STAR_shared_STAR_42579,_STAR_instrument_STAR_42580,_STAR_parent_STAR_42581,this$,this__20041__auto__,x42568_42591){
return (function (){
return com.wsscode.oge.core.oge_query(this$,query);
});})(map__42582,map__42582__$1,query,result,profile,result_SINGLEQUOTE_,indexes,map__42583,map__42583__$1,style,css,_STAR_reconciler_STAR_42577,_STAR_depth_STAR_42578,_STAR_shared_STAR_42579,_STAR_instrument_STAR_42580,_STAR_parent_STAR_42581,this$,this__20041__auto__,x42568_42591))
], null);
var G__42587 = (cljs.core.truth_(fulcro.client.data_fetch.loading_QMARK_(cljs.core.cst$kw$ui_SLASH_fetch_DASH_state.cljs$core$IFn$_invoke$arity$1(result_SINGLEQUOTE_)))?"Loading...":"Run Query");
return (com.wsscode.oge.ui.common.button.cljs$core$IFn$_invoke$arity$2 ? com.wsscode.oge.ui.common.button.cljs$core$IFn$_invoke$arity$2(G__42586,G__42587) : com.wsscode.oge.ui.common.button.call(null,G__42586,G__42587));
})()], 0)),com.wsscode.oge.ui.codemirror.oge(new cljs.core.PersistentArrayMap(null, 5, [cljs.core.cst$kw$className,cljs.core.cst$kw$editor.cljs$core$IFn$_invoke$arity$1(css),cljs.core.cst$kw$value,(function (){var or__10412__auto__ = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(query)].join('');
if(cljs.core.truth_(or__10412__auto__)){
return or__10412__auto__;
} else {
return "";
}
})(),cljs.core.cst$kw$com$wsscode$pathom$connect_SLASH_indexes,com.wsscode.pathom.core.elide_not_found(indexes),cljs.core.cst$kw$com$wsscode$oge$ui$codemirror_SLASH_options,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$com$wsscode$oge$ui$codemirror_SLASH_extraKeys,new cljs.core.PersistentArrayMap(null, 5, ["Cmd-Enter",((function (map__42582,map__42582__$1,query,result,profile,result_SINGLEQUOTE_,indexes,map__42583,map__42583__$1,style,css,_STAR_reconciler_STAR_42577,_STAR_depth_STAR_42578,_STAR_shared_STAR_42579,_STAR_instrument_STAR_42580,_STAR_parent_STAR_42581,this$,this__20041__auto__,x42568_42591){
return (function (_){
return com.wsscode.oge.core.oge_query(this$,cljs.core.cst$kw$oge_SLASH_query.cljs$core$IFn$_invoke$arity$1(om.next.props(this$)));
});})(map__42582,map__42582__$1,query,result,profile,result_SINGLEQUOTE_,indexes,map__42583,map__42583__$1,style,css,_STAR_reconciler_STAR_42577,_STAR_depth_STAR_42578,_STAR_shared_STAR_42579,_STAR_instrument_STAR_42580,_STAR_parent_STAR_42581,this$,this__20041__auto__,x42568_42591))
,"Ctrl-Enter",((function (map__42582,map__42582__$1,query,result,profile,result_SINGLEQUOTE_,indexes,map__42583,map__42583__$1,style,css,_STAR_reconciler_STAR_42577,_STAR_depth_STAR_42578,_STAR_shared_STAR_42579,_STAR_instrument_STAR_42580,_STAR_parent_STAR_42581,this$,this__20041__auto__,x42568_42591){
return (function (_){
return com.wsscode.oge.core.oge_query(this$,cljs.core.cst$kw$oge_SLASH_query.cljs$core$IFn$_invoke$arity$1(om.next.props(this$)));
});})(map__42582,map__42582__$1,query,result,profile,result_SINGLEQUOTE_,indexes,map__42583,map__42583__$1,style,css,_STAR_reconciler_STAR_42577,_STAR_depth_STAR_42578,_STAR_shared_STAR_42579,_STAR_instrument_STAR_42580,_STAR_parent_STAR_42581,this$,this__20041__auto__,x42568_42591))
,"Shift-Enter",((function (map__42582,map__42582__$1,query,result,profile,result_SINGLEQUOTE_,indexes,map__42583,map__42583__$1,style,css,_STAR_reconciler_STAR_42577,_STAR_depth_STAR_42578,_STAR_shared_STAR_42579,_STAR_instrument_STAR_42580,_STAR_parent_STAR_42581,this$,this__20041__auto__,x42568_42591){
return (function (_){
return com.wsscode.oge.core.oge_query(this$,cljs.core.cst$kw$oge_SLASH_query.cljs$core$IFn$_invoke$arity$1(om.next.props(this$)));
});})(map__42582,map__42582__$1,query,result,profile,result_SINGLEQUOTE_,indexes,map__42583,map__42583__$1,style,css,_STAR_reconciler_STAR_42577,_STAR_depth_STAR_42578,_STAR_shared_STAR_42579,_STAR_instrument_STAR_42580,_STAR_parent_STAR_42581,this$,this__20041__auto__,x42568_42591))
,"Cmd-J","ogeJoin","Ctrl-Space","autocomplete"], null)], null),cljs.core.cst$kw$onChange,((function (map__42582,map__42582__$1,query,result,profile,result_SINGLEQUOTE_,indexes,map__42583,map__42583__$1,style,css,_STAR_reconciler_STAR_42577,_STAR_depth_STAR_42578,_STAR_shared_STAR_42579,_STAR_instrument_STAR_42580,_STAR_parent_STAR_42581,this$,this__20041__auto__,x42568_42591){
return (function (p1__42565_SHARP_){
return fulcro.client.mutations.set_value_BANG_(this$,cljs.core.cst$kw$oge_SLASH_query,p1__42565_SHARP_);
});})(map__42582,map__42582__$1,query,result,profile,result_SINGLEQUOTE_,indexes,map__42583,map__42583__$1,style,css,_STAR_reconciler_STAR_42577,_STAR_depth_STAR_42578,_STAR_shared_STAR_42579,_STAR_instrument_STAR_42580,_STAR_parent_STAR_42581,this$,this__20041__auto__,x42568_42591))
], null)),om.dom.div(({"className": cljs.core.cst$kw$divisor.cljs$core$IFn$_invoke$arity$1(css)})),com.wsscode.oge.ui.codemirror.clojure(new cljs.core.PersistentArrayMap(null, 3, [cljs.core.cst$kw$className,cljs.core.cst$kw$result.cljs$core$IFn$_invoke$arity$1(css),cljs.core.cst$kw$value,(function (){var or__10412__auto__ = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(result)].join('');
if(cljs.core.truth_(or__10412__auto__)){
return or__10412__auto__;
} else {
return "";
}
})(),cljs.core.cst$kw$com$wsscode$oge$ui$codemirror_SLASH_options,new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$com$wsscode$oge$ui$codemirror_SLASH_readOnly,true,cljs.core.cst$kw$com$wsscode$oge$ui$codemirror_SLASH_lineNumbers,true], null)], null)),(cljs.core.truth_(profile)?om.dom.div(({"className": cljs.core.cst$kw$hdiv.cljs$core$IFn$_invoke$arity$1(css)})):null),(cljs.core.truth_(profile)?om.dom.div.cljs$core$IFn$_invoke$arity$variadic(({"className": cljs.core.cst$kw$flame.cljs$core$IFn$_invoke$arity$1(css)}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (){var G__42588 = new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$profile,profile], null);
return (com.wsscode.oge.ui.flame_graph.flame_graph.cljs$core$IFn$_invoke$arity$1 ? com.wsscode.oge.ui.flame_graph.flame_graph.cljs$core$IFn$_invoke$arity$1(G__42588) : com.wsscode.oge.ui.flame_graph.flame_graph.call(null,G__42588));
})()], 0)):null)], 0));
}finally {om.next._STAR_parent_STAR_ = _STAR_parent_STAR_42581;

om.next._STAR_instrument_STAR_ = _STAR_instrument_STAR_42580;

om.next._STAR_shared_STAR_ = _STAR_shared_STAR_42579;

om.next._STAR_depth_STAR_ = _STAR_depth_STAR_42578;

om.next._STAR_reconciler_STAR_ = _STAR_reconciler_STAR_42577;
}});})(x42568_42591))
;


com.wsscode.oge.core.Oge.prototype.constructor = com.wsscode.oge.core.Oge;

com.wsscode.oge.core.Oge.prototype.constructor.displayName = "com.wsscode.oge.core/Oge";

com.wsscode.oge.core.Oge.prototype.om$isComponent = true;

var x42589_42595 = com.wsscode.oge.core.Oge;
/** @nocollapse */
x42589_42595.fulcro$client$core$InitialAppState$ = cljs.core.PROTOCOL_SENTINEL;

/** @nocollapse */
x42589_42595.fulcro$client$core$InitialAppState$initial_state$arity$2 = ((function (x42589_42595){
return (function (_,___$1){
var ___$2 = this;
return new cljs.core.PersistentArrayMap(null, 4, [cljs.core.cst$kw$oge_SLASH_id,"editor",cljs.core.cst$kw$oge_SLASH_query,"[]",cljs.core.cst$kw$oge_SLASH_result,"{}",cljs.core.cst$kw$oge_SLASH_profile,null], null);
});})(x42589_42595))
;

/** @nocollapse */
x42589_42595.om$next$IQuery$ = cljs.core.PROTOCOL_SENTINEL;

/** @nocollapse */
x42589_42595.om$next$IQuery$query$arity$1 = ((function (x42589_42595){
return (function (_){
var ___$1 = this;
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$oge_SLASH_id,cljs.core.cst$kw$oge_SLASH_query,cljs.core.cst$kw$oge_SLASH_result,cljs.core.cst$kw$oge_SLASH_profile,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$com$wsscode$pathom$connect_SLASH_indexes,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$com$wsscode$pathom$connect_SLASH_idents,cljs.core.cst$kw$com$wsscode$pathom$connect_SLASH_index_DASH_io,cljs.core.cst$kw$com$wsscode$pathom$connect_SLASH_autocomplete_DASH_ignore,cljs.core.cst$kw$ui_SLASH_fetch_DASH_state], null)], null),new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$oge_SLASH_result_SINGLEQUOTE_,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$ui_SLASH_fetch_DASH_state], null)], null)], null);
});})(x42589_42595))
;

/** @nocollapse */
x42589_42595.om$next$Ident$ = cljs.core.PROTOCOL_SENTINEL;

/** @nocollapse */
x42589_42595.om$next$Ident$ident$arity$2 = ((function (x42589_42595){
return (function (_,props){
var ___$1 = this;
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$oge_SLASH_id,cljs.core.cst$kw$oge_SLASH_id.cljs$core$IFn$_invoke$arity$1(props)], null);
});})(x42589_42595))
;

/** @nocollapse */
x42589_42595.fulcro_css$css$CSS$ = cljs.core.PROTOCOL_SENTINEL;

/** @nocollapse */
x42589_42595.fulcro_css$css$CSS$local_rules$arity$1 = ((function (x42589_42595){
return (function (_){
var ___$1 = this;
return new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$$container,new cljs.core.PersistentArrayMap(null, 6, [cljs.core.cst$kw$display,"grid",cljs.core.cst$kw$width,"100%",cljs.core.cst$kw$border,"1px solid #ddd",cljs.core.cst$kw$grid_DASH_template_DASH_rows,"auto 1fr 12px 300px",cljs.core.cst$kw$grid_DASH_template_DASH_columns,"600px 12px 1fr",cljs.core.cst$kw$grid_DASH_template_DASH_areas,com.wsscode.oge.ui.helpers.strings(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["title title title","editor divisor result","hdiv divisor result","flame divisor result"], null))], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$_AMPERSAND_$simple,new cljs.core.PersistentArrayMap(null, 3, [cljs.core.cst$kw$grid_DASH_template_DASH_rows,"auto 1fr",cljs.core.cst$kw$grid_DASH_template_DASH_columns,"600px 12px 1fr",cljs.core.cst$kw$grid_DASH_template_DASH_areas,com.wsscode.oge.ui.helpers.strings(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["title title title","editor divisor result"], null))], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$$CodeMirror,new cljs.core.PersistentArrayMap(null, 3, [cljs.core.cst$kw$height,"100%",cljs.core.cst$kw$width,"100%",cljs.core.cst$kw$position,"absolute"], null)], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$$title,new cljs.core.PersistentArrayMap(null, 6, [cljs.core.cst$kw$grid_DASH_area,"title",cljs.core.cst$kw$display,"flex",cljs.core.cst$kw$align_DASH_items,"center",cljs.core.cst$kw$background,"linear-gradient(#f7f7f7, #e2e2e2)",cljs.core.cst$kw$padding,"2px 10px",cljs.core.cst$kw$border_DASH_bottom,"1px solid #e0e0e0"], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$$title_DASH_oge,new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$margin,"0",cljs.core.cst$kw$margin_DASH_bottom,"8px"], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$$divisor,new cljs.core.PersistentArrayMap(null, 5, [cljs.core.cst$kw$grid_DASH_area,"divisor",cljs.core.cst$kw$background,"#eee",cljs.core.cst$kw$border,"1px solid #e0e0e0",cljs.core.cst$kw$border_DASH_top,"0",cljs.core.cst$kw$border_DASH_bottom,"0"], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$$editor,new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$grid_DASH_area,"editor",cljs.core.cst$kw$position,"relative"], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$$result,new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$grid_DASH_area,"result",cljs.core.cst$kw$position,"relative"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$$CodeMirror,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$background,"#f6f7f8"], null)], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$$hdiv,new cljs.core.PersistentArrayMap(null, 4, [cljs.core.cst$kw$grid_DASH_area,"hdiv",cljs.core.cst$kw$background,"#eee",cljs.core.cst$kw$border,"1px solid #e0e0e0",cljs.core.cst$kw$border_DASH_width,"1px 0"], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$$flame,new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$grid_DASH_area,"flame",cljs.core.cst$kw$background,"#f6f7f8"], null)], null)], null);
});})(x42589_42595))
;

/** @nocollapse */
x42589_42595.fulcro_css$css$CSS$include_children$arity$1 = ((function (x42589_42595){
return (function (_){
var ___$1 = this;
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [com.wsscode.oge.ui.common.CSS], null);
});})(x42589_42595))
;


var x42590_42596 = com.wsscode.oge.core.Oge.prototype;

x42590_42596.fulcro$client$core$InitialAppState$ = cljs.core.PROTOCOL_SENTINEL;


x42590_42596.fulcro$client$core$InitialAppState$initial_state$arity$2 = ((function (x42590_42596){
return (function (_,___$1){
var ___$2 = this;
return new cljs.core.PersistentArrayMap(null, 4, [cljs.core.cst$kw$oge_SLASH_id,"editor",cljs.core.cst$kw$oge_SLASH_query,"[]",cljs.core.cst$kw$oge_SLASH_result,"{}",cljs.core.cst$kw$oge_SLASH_profile,null], null);
});})(x42590_42596))
;


x42590_42596.om$next$IQuery$ = cljs.core.PROTOCOL_SENTINEL;


x42590_42596.om$next$IQuery$query$arity$1 = ((function (x42590_42596){
return (function (_){
var ___$1 = this;
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$oge_SLASH_id,cljs.core.cst$kw$oge_SLASH_query,cljs.core.cst$kw$oge_SLASH_result,cljs.core.cst$kw$oge_SLASH_profile,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$com$wsscode$pathom$connect_SLASH_indexes,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$com$wsscode$pathom$connect_SLASH_idents,cljs.core.cst$kw$com$wsscode$pathom$connect_SLASH_index_DASH_io,cljs.core.cst$kw$com$wsscode$pathom$connect_SLASH_autocomplete_DASH_ignore,cljs.core.cst$kw$ui_SLASH_fetch_DASH_state], null)], null),new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$oge_SLASH_result_SINGLEQUOTE_,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$ui_SLASH_fetch_DASH_state], null)], null)], null);
});})(x42590_42596))
;


x42590_42596.om$next$Ident$ = cljs.core.PROTOCOL_SENTINEL;


x42590_42596.om$next$Ident$ident$arity$2 = ((function (x42590_42596){
return (function (_,props){
var ___$1 = this;
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$oge_SLASH_id,cljs.core.cst$kw$oge_SLASH_id.cljs$core$IFn$_invoke$arity$1(props)], null);
});})(x42590_42596))
;


x42590_42596.fulcro_css$css$CSS$ = cljs.core.PROTOCOL_SENTINEL;


x42590_42596.fulcro_css$css$CSS$local_rules$arity$1 = ((function (x42590_42596){
return (function (_){
var ___$1 = this;
return new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$$container,new cljs.core.PersistentArrayMap(null, 6, [cljs.core.cst$kw$display,"grid",cljs.core.cst$kw$width,"100%",cljs.core.cst$kw$border,"1px solid #ddd",cljs.core.cst$kw$grid_DASH_template_DASH_rows,"auto 1fr 12px 300px",cljs.core.cst$kw$grid_DASH_template_DASH_columns,"600px 12px 1fr",cljs.core.cst$kw$grid_DASH_template_DASH_areas,com.wsscode.oge.ui.helpers.strings(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["title title title","editor divisor result","hdiv divisor result","flame divisor result"], null))], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$_AMPERSAND_$simple,new cljs.core.PersistentArrayMap(null, 3, [cljs.core.cst$kw$grid_DASH_template_DASH_rows,"auto 1fr",cljs.core.cst$kw$grid_DASH_template_DASH_columns,"600px 12px 1fr",cljs.core.cst$kw$grid_DASH_template_DASH_areas,com.wsscode.oge.ui.helpers.strings(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["title title title","editor divisor result"], null))], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$$CodeMirror,new cljs.core.PersistentArrayMap(null, 3, [cljs.core.cst$kw$height,"100%",cljs.core.cst$kw$width,"100%",cljs.core.cst$kw$position,"absolute"], null)], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$$title,new cljs.core.PersistentArrayMap(null, 6, [cljs.core.cst$kw$grid_DASH_area,"title",cljs.core.cst$kw$display,"flex",cljs.core.cst$kw$align_DASH_items,"center",cljs.core.cst$kw$background,"linear-gradient(#f7f7f7, #e2e2e2)",cljs.core.cst$kw$padding,"2px 10px",cljs.core.cst$kw$border_DASH_bottom,"1px solid #e0e0e0"], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$$title_DASH_oge,new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$margin,"0",cljs.core.cst$kw$margin_DASH_bottom,"8px"], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$$divisor,new cljs.core.PersistentArrayMap(null, 5, [cljs.core.cst$kw$grid_DASH_area,"divisor",cljs.core.cst$kw$background,"#eee",cljs.core.cst$kw$border,"1px solid #e0e0e0",cljs.core.cst$kw$border_DASH_top,"0",cljs.core.cst$kw$border_DASH_bottom,"0"], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$$editor,new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$grid_DASH_area,"editor",cljs.core.cst$kw$position,"relative"], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$$result,new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$grid_DASH_area,"result",cljs.core.cst$kw$position,"relative"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$$CodeMirror,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$background,"#f6f7f8"], null)], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$$hdiv,new cljs.core.PersistentArrayMap(null, 4, [cljs.core.cst$kw$grid_DASH_area,"hdiv",cljs.core.cst$kw$background,"#eee",cljs.core.cst$kw$border,"1px solid #e0e0e0",cljs.core.cst$kw$border_DASH_width,"1px 0"], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$$flame,new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$grid_DASH_area,"flame",cljs.core.cst$kw$background,"#f6f7f8"], null)], null)], null);
});})(x42590_42596))
;


x42590_42596.fulcro_css$css$CSS$include_children$arity$1 = ((function (x42590_42596){
return (function (_){
var ___$1 = this;
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [com.wsscode.oge.ui.common.CSS], null);
});})(x42590_42596))
;


com.wsscode.oge.core.Oge.cljs$lang$type = true;

com.wsscode.oge.core.Oge.cljs$lang$ctorStr = "com.wsscode.oge.core/Oge";

com.wsscode.oge.core.Oge.cljs$lang$ctorPrWriter = (function (this__20295__auto__,writer__20296__auto__,opt__20297__auto__){
return cljs.core._write(writer__20296__auto__,"com.wsscode.oge.core/Oge");
});
com.wsscode.oge.core.oge = om.next.factory.cljs$core$IFn$_invoke$arity$1(com.wsscode.oge.core.Oge);
