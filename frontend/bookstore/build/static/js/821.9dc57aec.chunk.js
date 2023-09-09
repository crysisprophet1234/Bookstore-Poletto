"use strict";(self.webpackChunkbookstore=self.webpackChunkbookstore||[]).push([[821],{1821:function(e,t,a){a.r(t),a.d(t,{default:function(){return N}});var n=a(3433),r=a(1413),o=a(9439),i=a(2791),s=a(1134),c=a(7689),u=a(8622),l=a(7462),d=a(5817),m=a(8003),f=a(4942),v=a(4925),p=a(1188),h=["defaultOptions","cacheOptions","loadOptions","options","isLoading","onInputChange","filterOption"];a(4164),a(1672);var b=(0,i.forwardRef)((function(e,t){var a=function(e){var t=e.defaultOptions,a=void 0!==t&&t,n=e.cacheOptions,s=void 0!==n&&n,c=e.loadOptions;e.options;var u=e.isLoading,l=void 0!==u&&u,d=e.onInputChange,m=e.filterOption,b=void 0===m?null:m,g=(0,v.Z)(e,h),Z=g.inputValue,x=(0,i.useRef)(void 0),j=(0,i.useRef)(!1),N=(0,i.useState)(Array.isArray(a)?a:void 0),k=(0,o.Z)(N,2),C=k[0],S=k[1],A=(0,i.useState)("undefined"!==typeof Z?Z:""),O=(0,o.Z)(A,2),L=O[0],D=O[1],y=(0,i.useState)(!0===a),R=(0,o.Z)(y,2),U=R[0],w=R[1],E=(0,i.useState)(void 0),I=(0,o.Z)(E,2),V=I[0],Q=I[1],q=(0,i.useState)([]),P=(0,o.Z)(q,2),T=P[0],K=P[1],$=(0,i.useState)(!1),z=(0,o.Z)($,2),B=z[0],F=z[1],M=(0,i.useState)({}),G=(0,o.Z)(M,2),H=G[0],J=G[1],W=(0,i.useState)(void 0),X=(0,o.Z)(W,2),Y=X[0],_=X[1],ee=(0,i.useState)(void 0),te=(0,o.Z)(ee,2),ae=te[0],ne=te[1];s!==ae&&(J({}),ne(s)),a!==Y&&(S(Array.isArray(a)?a:void 0),_(a)),(0,i.useEffect)((function(){return j.current=!0,function(){j.current=!1}}),[]);var re=(0,i.useCallback)((function(e,t){if(!c)return t();var a=c(e,t);a&&"function"===typeof a.then&&a.then(t,(function(){return t()}))}),[c]);(0,i.useEffect)((function(){!0===a&&re(L,(function(e){j.current&&(S(e||[]),w(!!x.current))}))}),[]);var oe=(0,i.useCallback)((function(e,t){var a=(0,p.L)(e,t,d);if(!a)return x.current=void 0,D(""),Q(""),K([]),w(!1),void F(!1);if(s&&H[a])D(a),Q(a),K(H[a]),w(!1),F(!1);else{var n=x.current={};D(a),w(!0),F(!V),re(a,(function(e){j&&n===x.current&&(x.current=void 0,w(!1),Q(a),K(e||[]),F(!1),J(e?(0,r.Z)((0,r.Z)({},H),{},(0,f.Z)({},a,e)):H))}))}}),[s,re,V,H,d]),ie=B?[]:L&&V?T:C||[];return(0,r.Z)((0,r.Z)({},g),{},{options:ie,isLoading:U||l,onInputChange:oe,filterOption:b})}(e),n=(0,m.u)(a);return i.createElement(d.S,(0,l.Z)({ref:t},n))})),g=a(5069),Z=a(9085),x=a(2887),j=a(184),N=function(){var e,t,a,l=(0,c.UO)().bookId,d=(0,i.useState)(),m=(0,o.Z)(d,2),f=m[0],v=m[1],p="create"!==l,h=(0,c.s0)(),N=(0,i.useState)([]),k=(0,o.Z)(N,2),C=k[0],S=k[1],A=(0,s.cI)(),O=A.register,L=A.handleSubmit,D=A.formState.errors,y=A.setValue,R=A.control;(0,i.useEffect)((function(){(0,g.QK)({url:"/api/categories/v2"}).then((function(e){S(e.data)}))}),[]);var U=(0,x.D)((function(e,t){(0,g.QK)({url:"/api/authors/v2/all"}).then((function(a){t(a.data.filter((function(t){return t.name.toLowerCase().includes(e.toLowerCase())})))})).catch((function(e){console.log(e)}))}),500);(0,i.useEffect)((function(){p&&(0,g.QK)({url:"/api/books/v2/".concat(l)}).then((function(e){var t=e.data;y("name",t.name),y("releaseDate",t.releaseDate),y("author",t.author),y("imgUrl",t.imgUrl),y("categories",t.categories),v(t)}))}),[p,l,y]);return(0,j.jsx)("div",{className:"product-crud-container",children:(0,j.jsxs)("div",{className:"base-card product-crud-form-card",children:[(0,j.jsxs)("h1",{className:"product-crud-form-title",children:["DADOS DO LIVRO ",p&&" - c\xf3digo ".concat(l)]}),p&&(0,j.jsx)("h2",{className:"product-crud-form-status",children:"AVAILABLE"===(null===f||void 0===f?void 0:f.status)?"Dispon\xedvel":"Reservado"}),(0,j.jsxs)("form",{onSubmit:L((function(e){var t=(0,r.Z)((0,r.Z)({},e),{},{status:"AVAILABLE"}),a={method:p?"PUT":"POST",url:p?"/api/books/v2/".concat(l):"/api/books/v2",data:t,withCredentials:!0};(0,g.QK)(a).then((function(){Z.Am.info("Livro #".concat(l," ").concat(p?"atualizado":"cadastrado"," com sucesso"),{autoClose:3e3}),h("/admin/books")})).catch((function(e){console.log(e),Z.Am.error("Falha ao cadastrar livro: \n ".concat(e.response.data.message))}))})),children:[(0,j.jsx)("div",{className:"row product-crud-inputs-container",children:(0,j.jsxs)("div",{className:"col-lg-6 product-crud-inputs-left-container",children:[(0,j.jsxs)("div",{className:"margin-bottom-30",children:[(0,j.jsx)("input",(0,r.Z)((0,r.Z)({},O("name",{required:"Campo obrigat\xf3rio",pattern:{value:/^[A-Za-z0-9'& -#]+$/,message:"Nome deve conter apenas letras e n\xfameros"}})),{},{type:"text",className:"form-control base-input ".concat(D.name?"is-invalid":""),placeholder:"T\xedtulo do livro",name:"name"})),(0,j.jsx)("div",{className:"invalid-feedback d-block",children:null===(e=D.name)||void 0===e?void 0:e.message})]}),(0,j.jsxs)("div",{className:"margin-bottom-30 ",children:[(0,j.jsx)(s.Qr,{name:"categories",rules:{required:!0},control:R,render:function(e){var t=e.field;return(0,j.jsx)(u.ZP,(0,r.Z)((0,r.Z)({},t),{},{options:C,isClearable:!0,classNamePrefix:"product-crud-select",isMulti:!0,onChange:function(e){return y("categories",(0,n.Z)(e))},getOptionLabel:function(e){return e.name},getOptionValue:function(e){return String(e.id)},placeholder:"Categoria"}))}}),D.categories&&(0,j.jsx)("div",{className:"invalid-feedback d-block",children:"Campo obrigat\xf3rio"})]}),(0,j.jsxs)("div",{className:"margin-bottom-30 ",children:[(0,j.jsx)(s.Qr,{name:"author",rules:{required:!0},control:R,render:function(e){var t=e.field;return(0,j.jsx)(b,(0,r.Z)((0,r.Z)({},t),{},{loadOptions:U,isClearable:!0,isSearchable:!0,classNamePrefix:"product-crud-select",onChange:function(e){return y("author",e)},getOptionLabel:function(e){return e.name},getOptionValue:function(e){return String(e.id)},placeholder:"Autor"}))}}),D.author&&(0,j.jsx)("div",{className:"invalid-feedback d-block",children:"Campo obrigat\xf3rio"})]}),(0,j.jsxs)("div",{className:"margin-bottom-30",children:[(0,j.jsx)("input",(0,r.Z)((0,r.Z)({},O("imgUrl",{required:"Campo obrigat\xf3rio",pattern:{value:/^(https?|chrome):\/\/[^\s$.?#].[^\s]*$/gm,message:"Deve ser uma URL v\xe1lida"}})),{},{type:"text",className:"form-control base-input ".concat(D.imgUrl?"is-invalid":""),placeholder:"URL da imagem do livro",name:"imgUrl"})),(0,j.jsx)("div",{className:"invalid-feedback d-block",children:null===(t=D.imgUrl)||void 0===t?void 0:t.message})]}),(0,j.jsxs)("div",{className:"margin-bottom-30",children:[(0,j.jsx)("input",(0,r.Z)((0,r.Z)({},O("releaseDate",{required:"Campo obrigat\xf3rio"})),{},{type:"date",className:"form-control base-input ".concat(D.releaseDate?"is-invalid":""),placeholder:"Release date",name:"releaseDate"})),(0,j.jsx)("div",{className:"invalid-feedback d-block",children:null===(a=D.releaseDate)||void 0===a?void 0:a.message})]})]})}),(0,j.jsxs)("div",{className:"product-crud-buttons-container",children:[(0,j.jsx)("button",{className:"btn btn-outline-danger product-crud-button",onClick:function(){h("/admin/books")},children:"CANCELAR"}),(0,j.jsx)("button",{className:"btn btn-primary product-crud-button text-white",children:"SALVAR"})]})]})]})})}},2887:function(e,t,a){a.d(t,{D:function(){return n}});var n=function(e,t){var a;return function(){for(var n=arguments.length,r=new Array(n),o=0;o<n;o++)r[o]=arguments[o];clearTimeout(a),a=setTimeout((function(){e.apply(void 0,r)}),t)}}}}]);
//# sourceMappingURL=821.9dc57aec.chunk.js.map