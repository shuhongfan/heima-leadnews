let utilFunc = {
    initIconFont () {
        let domModule = weex.requireModule('dom');
        domModule.addRule('fontFace', {
            'fontFamily': "fontawesome",
            'src': "url('https://cdn.bootcss.com/font-awesome/4.7.0/fonts/fontawesome-webfont.ttf?v=5.10.2')"
        });
    }
};

export default utilFunc;
